package com.application.alphacapital.superapp.vault.activity

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.alphaestatevault.utils.ImageLoadingUtils
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.vault.view.VaultCropImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class VaultCropActivity : VaultBaseActivity()
{
    lateinit var mCropView: VaultCropImageView
    lateinit var tvCancel: TextView
    lateinit var ivrotation: ImageView
    lateinit var tvCrop: TextView
    lateinit var llLoading: LinearLayout

    var imagePath: String = ""
    var isForSave: Boolean = false
    lateinit var cropped: Bitmap
    lateinit var bitmapTemp: Bitmap
    var outputSize: Int = 1000
    var degree: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.vault_activity_crop)

        appUtils.setBlackStatusBar(activity)

        activity = this

        imagePath = intent.getStringExtra("imgPath").toString()

        outputSize = intent.getIntExtra("outputSize", 1000)
        // compress
        val newFilePath = compressImage(imagePath)
        //File file = new File(imagePath);

        initView()

        onclickEvents()

        if (newFilePath == null || !newFilePath.exists())
        {
            Toast.makeText(activity, "File does not exists!", Toast.LENGTH_SHORT).show()
            finish()
            finishActivityAnimation()
        }
        else
        {
            setCropImage(imagePath)
        }
    }

    private fun setCropImage(path: String)
    {
        try
        {
            degree = mCropView.getRotation()
            mCropView.setCropMode(VaultCropImageView.CropMode.RATIO_FREE)
            bitmapTemp = appUtils.rotateBitmapNew(imagePath, degree)
            mCropView.setImageBitmap(bitmapTemp)
        }
        catch (e: Exception)
        {
        }
    }

    private fun initView()
    {
        mCropView = findViewById<View>(R.id.cropImageView) as VaultCropImageView
        tvCancel = findViewById<View>(R.id.tvCancel) as TextView
        ivrotation = findViewById<View>(R.id.ivrotation) as ImageView
        tvCrop = findViewById<View>(R.id.tvCrop) as TextView
        llLoading = findViewById<View>(R.id.llLoading) as LinearLayout
    }

    fun compressImage(filePath: String?): File?
    {
        val utils = ImageLoadingUtils(activity!!)
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        if (bmp == null)
        {
            bmp = BitmapFactory.decodeResource(activity!!.resources, R.drawable.vault_blank)
        }
        var actualHeight = 0
        var actualWidth = 0
        if (options.outWidth > 0 || options.outHeight > 0)
        {
            actualHeight = options.outHeight
            actualWidth = options.outWidth
        }
        else
        {
            actualHeight = 1000
            actualWidth = 800
        }
        val maxHeight = 1006.0f
        val maxWidth = 812.0f
        var imgRatio = actualWidth / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight
        if (actualHeight > maxHeight || actualWidth > maxWidth)
        {
            if (imgRatio < maxRatio)
            {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            }
            else if (imgRatio > maxRatio)
            {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            }
            else
            {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
        options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try
        {
            bmp = BitmapFactory.decodeFile(filePath, options)
        }
        catch (exception: OutOfMemoryError)
        {
            exception.printStackTrace()
        }
        if (bmp == null)
        {
            bmp = BitmapFactory.decodeResource(activity!!.resources, R.drawable.vault_blank)
        }
        try
        {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        }
        catch (exception: OutOfMemoryError)
        {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bmp!!, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))
        var tostoreFile: File? = null
        val exif: ExifInterface
        return try
        {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6)
            {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            }
            else if (orientation == 3)
            {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            }
            else if (orientation == 8)
            {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
            val dirtostoreFile = File(Environment.getExternalStorageDirectory().toString() + "/Fingers/.Media/Images/")
            if (!dirtostoreFile.exists())
            {
                dirtostoreFile.mkdirs()
            }
            var out: FileOutputStream? = null
            val timestr = Calendar.getInstance().timeInMillis.toString()
            Log.e("TIME STRING ", "compressImage: $timestr")
            tostoreFile = File(Environment.getExternalStorageDirectory().toString() + "/Fingers/.Media/Images/IMG_" + timestr + ".jpg")
            out = FileOutputStream(tostoreFile.absolutePath)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            tostoreFile
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            null
        }
    }

    private fun onclickEvents()
    {
        tvCrop!!.setOnClickListener {
            if (!isForSave)
            {
                isForSave = true
                cropped = mCropView!!.croppedBitmap
                cropAsync()
            }
        }

        ivrotation!!.setOnClickListener { view ->
            try
            {
                if (degree > 350)
                {
                    degree = 0f
                }
                else
                {
                    degree = degree + 90
                }

                bitmapTemp = appUtils.rotateBitmapNew(imagePath, degree)
                mCropView!!.setImageBitmap(bitmapTemp)
            }
            catch (e: Exception)
            {
            }
        }
        tvCancel!!.setOnClickListener { view ->
            finish()
            finishActivityAnimation()
        }
    }

    private fun cropAsync()
    {
        object : AsyncTask<Void?, Void?, Void?>()
        {
            private var path: String? = ""

            override fun onPreExecute()
            {
                visible(llLoading)
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): Void?
            {
                try
                {
                    var tostoreFile: File? = null
                    var out: FileOutputStream? = null
                    try
                    {
                        val dirtostoreFile = File(Environment.getExternalStorageDirectory().toString() + "/Aqualogy/.Media/Images/Cropped/")
                        if (!dirtostoreFile.exists())
                        {
                            dirtostoreFile.mkdirs()
                        }
                        val timestr = Calendar.getInstance().timeInMillis.toString()
                        tostoreFile = File(Environment.getExternalStorageDirectory().toString() + "/Aqualogy/.Media/Images/Cropped/IMG_" + timestr + ".jpg")
                        out = FileOutputStream(tostoreFile.absolutePath)
                        cropped!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                    finally
                    {
                        try
                        {
                            out?.close()
                        }
                        catch (e: IOException)
                        {
                            e.printStackTrace()
                        }
                    }
                    val file = cropAndCompressImage(tostoreFile!!.absolutePath, outputSize, outputSize.toFloat())
                    if (file != null && file.exists())
                    {
                        path = file.absolutePath
                        println(file.absolutePath + "")
                    }
                    else
                    {
                        path = ""
                        println("error saving file!!")
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
                return null
            }


            override fun onPostExecute(result: Void?)
            {
                super.onPostExecute(result)
                gone(llLoading)
                if (path != null && path!!.trim { it <= ' ' }.length > 0)
                {
                    try
                    {
                        val intentData = Intent().putExtra("single_path", path)
                        setResult(Activity.RESULT_OK, intentData)
                        finish()
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }


        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null as Void?)
    }

    fun cropAndCompressImage(filePath: String?, height: Int, heightF: Float): File?
    {
        val utils = ImageLoadingUtils(activity!!)
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        if (bmp == null)
        {
            bmp = BitmapFactory.decodeResource(activity!!.resources, R.drawable.vault_blank)
        }
        var actualHeight = 0
        var actualWidth = 0
        if (options.outWidth > 0 || options.outHeight > 0)
        {
            actualHeight = options.outHeight
            actualWidth = options.outWidth
        }
        else
        {
            if (height == 0)
            {
                actualHeight = 200
                actualWidth = 200
            }
            else
            {
                actualHeight = height
                actualWidth = height
            }
        }
        var maxHeight = 0.0f
        var maxWidth = 0.0f
        if (heightF.toDouble() == 0.0)
        {
            maxHeight = 200.0f
            maxWidth = 200.0f
        }
        else
        {
            maxHeight = heightF
            maxWidth = heightF
        }
        var imgRatio = actualWidth / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight
        if (actualHeight > maxHeight || actualWidth > maxWidth)
        {
            if (imgRatio < maxRatio)
            {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            }
            else if (imgRatio > maxRatio)
            {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            }
            else
            {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
        options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try
        {
            bmp = BitmapFactory.decodeFile(filePath, options)
        }
        catch (exception: OutOfMemoryError)
        {
            exception.printStackTrace()
        }
        if (bmp == null)
        {
            bmp = BitmapFactory.decodeResource(activity!!.resources, R.drawable.vault_blank)
        }
        try
        {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        }
        catch (exception: OutOfMemoryError)
        {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bmp!!, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))
        var tostoreFile: File? = null
        val exif: ExifInterface
        return try
        {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6)
            {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            }
            else if (orientation == 3)
            {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            }
            else if (orientation == 8)
            {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
            val dirtostoreFile = File(Environment.getExternalStorageDirectory().toString() + "/Fingers/.Media/Images/Cropped/")
            if (!dirtostoreFile.exists())
            {
                dirtostoreFile.mkdirs()
            }
            var out: FileOutputStream? = null
            val timestr = Calendar.getInstance().timeInMillis.toString()
            tostoreFile = File(Environment.getExternalStorageDirectory().toString() + "/Fingers/.Media/Images/Cropped/IMG_" + timestr + ".jpg")
            out = FileOutputStream(tostoreFile.absolutePath)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            tostoreFile
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            null
        }
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        finish()
        finishActivityAnimation()
    }
}
