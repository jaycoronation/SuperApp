package com.application.alphacapital.superapp.finplan.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.application.alphacapital.superapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class AppUtils
{

    companion object
    {
        fun loadingDialog(activity: Activity):Dialog{
            val dialog = Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar)
            dialog.setContentView(R.layout.fin_plan_dialog_loading)

            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)

            val window: Window = dialog.window!!
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(activity, R.color.fin_plan_white)
            val wlp = window.attributes
            wlp.gravity = Gravity.CENTER
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
            window.attributes = wlp
            dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            return dialog
        }

        fun convertToCommaSeperatedValue(value: String): String
        {
            var str = ""
            try
            {
                val formatter = DecimalFormat("#,##,###")
                str = ""
                str = formatter.format(java.lang.Double.parseDouble(value))
            }
            catch (e: NumberFormatException)
            {
                e.printStackTrace()
            }

            return str
        }
    }

    fun openDrawerLeft(drawerLayout: DrawerLayout)
    {
        try
        {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            {
                drawerLayout.closeDrawer(Gravity.LEFT)
            }
            else
            {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    fun setLightStatusBar(activity: Activity)
    {

        val window = activity.window
        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            activity.window.statusBarColor = Color.WHITE
        }
    }

    fun setPrimaryColorStatusBar(activity: Activity)
    {

        val window = activity.window
        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            activity.window.statusBarColor = activity.resources.getColor(R.color.fin_plan_colorPrimary)
        }
    }

    fun setBlackStatusBar(activity: Activity)
    {
        val window = activity.window
        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            activity.window.statusBarColor = activity.resources.getColor(R.color.fin_plan_black)
        }
    }

    fun rotateBitmap(src: String?): Bitmap?
    {
        val bitmap = BitmapFactory.decodeFile(src)
        try
        {
            val exif = ExifInterface(src.toString())
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val matrix = Matrix()
            when (orientation)
            {
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL ->
                {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_TRANSPOSE ->
                {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE ->
                {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
                ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_UNDEFINED -> return bitmap
                else -> return bitmap
            }
            return try
            {
                val oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()
                oriented
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                bitmap
            }
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
        return bitmap
    }

    fun rotateBitmapNew(src: String?, degree: Float): Bitmap
    {
        val bitmap = BitmapFactory.decodeFile(src)
        val matrix = Matrix().apply { postRotate(degree) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    fun sendMessageFromHandler(handler: Handler, any: Any, what: Int, arg1: Int, arg2: Int)
    {
        if(handler!=null){
            var message: Message = Message.obtain()
            message.what = what
            message.arg1 = arg1
            message.arg2 = arg2
            message.obj = any
            handler.sendMessage(message)
        }else{
            return
        }
    }

    fun setStatusBarForDialog(dialog: Dialog, activity: Activity)
    {
        val window = dialog.window
        val view = window!!.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            dialog.window!!.statusBarColor = activity.resources.getColor(R.color.fin_plan_transparent)
        }
    }

    fun setPrimaryColorStatusBarForDialog(dialog: Dialog, activity: Activity)
    {
        val window = dialog.window
        val view = window!!.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            dialog.window!!.statusBarColor = activity.resources.getColor(R.color.fin_plan_colorAccent)
        }
    }

    fun printKeyHash(activity: Activity)
    {
        try
        {
            val info = activity.packageManager.getPackageInfo("com.alphaestatevault", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        }
        catch (e: Exception)
        {
        }
    }

    fun isEmailValid(email: String): Boolean
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun printString(str: String)
    {
        System.out.print(str)
    }

    fun validPassword(password: String?): Boolean {
        val checks =
            arrayOf( // special character
                Pattern.compile("[!@#\\$%^&*()~`\\-=_+\\[\\]{}|:\\\";',\\./<>?]"),  // small character
                Pattern.compile("[a-zA-z]"),  // capital character
                // Pattern.compile("[A-Z]"),  // numeric character
                Pattern.compile("\\d"),  // 6 to 40 character length
                Pattern.compile("^.{6,40}$")
            )
        var ok = true
        for (check in checks) {
            ok = ok && check.matcher(password).find()
        }
        return ok
    }

    fun isPasswordValid(password: String): Boolean
    {

        val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}"
        var pattern: Pattern
        var matcher: Matcher

        pattern = Pattern.compile(PASSWORD_REGEX)
        matcher = pattern.matcher(password)

        return matcher.matches()
    }

    fun getValidAPIStringResponse(str: String): String
    {
        return if (str.equals("null", ignoreCase = true) || str.equals("<null>", ignoreCase = true))
        {
            ""
        }
        else
        {

            str
        }
    }

    fun getTypeface(activity: Activity):Typeface{
        return  Typeface.createFromAsset(activity.assets, "regular.ttf")
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateString(timestamp: Long, pattern: String): String
    {
        return try
        {
            val calendar: Calendar = Calendar.getInstance()
            val tz: TimeZone = TimeZone.getDefault()
            calendar.timeInMillis = timestamp
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat(pattern)
            val date: Date = calendar.time
            sdf.format(date)
        }
        catch (e: Exception)
        {
            printString(e.toString())
            ""
        }
    }

    fun removeLastChFromString(str: String): String
    {
        var finalString = ""
        try
        {
            if (str.isNotEmpty())
            {
                finalString = str.substring(0, str.length - 1)
            }
        }
        catch (e: Exception)
        {
            printString(e.toString())
            finalString = ""
        }

        return finalString
    }

    @SuppressLint("DefaultLocale")
    fun String.toDisplayCase(): String = split(" ").map { it.capitalize() }.joinToString(" ")

    fun toFirstCapitalString(str: String): String
    {
        return str.capitalize()
    }

    fun toDisplayCaseNew(str: String): String
    {
        if (str.isEmpty())
        {
            return ""
        }
        // Create a char array of given String
        val ch = str.toCharArray()
        for (i in 0 until str.length)
        {
            // If first character of a word is found
            if ((i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' '))
            {
                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z')
                {
                    // Convert into Upper-case
                    ch[i] = (ch[i] - 'a' + 'A'.toInt()).toChar()
                }
            }
            else if (ch[i] >= 'A' && ch[i] <= 'Z')
            // Convert into Lower-Case
                ch[i] = (ch[i] + 'a'.toInt() - 'A'.toInt()).toChar()// If apart from first character
            // Any one is in Upper-case
        }
        // Convert the char array to equivalent String
        val st = String(ch)
        return st
    }


    fun dpFromPx(px: Float, activity: Activity)
    {
        px / activity.resources.displayMetrics.density
    }

    fun pxFromDp(dp: Float, activity: Activity)
    {
        dp * activity.resources.displayMetrics.density
    }

    @SuppressLint("SimpleDateFormat")
    fun universalDateConvert(dateStr: String, fromPattern: String, toPattern: String): String
    {
        var newString = ""
        if (dateStr.isEmpty())
        {
            return ""
        }
        else
        {
            try
            {
                val sdf = SimpleDateFormat(fromPattern)
                val d = sdf.parse(dateStr)
                sdf.applyPattern(toPattern)
                newString = sdf.format(d)
            }
            catch (e: Exception)
            {
                newString = ""
            }

            return newString
        }
    }

    fun removeError(edt: EditText, inputLayout: TextInputLayout)
    {
        edt.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?)
            {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
                inputLayout.isErrorEnabled = false
            }
        })
    }



    fun createGradientFromColor(startColor: String, endColor: String, linearLayout: View, radius: Float)
    {
        val colors = intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor))
        val gd = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        gd.cornerRadius = radius
        gd.shape = GradientDrawable.RECTANGLE
        linearLayout.background = gd
    }

    fun convertToCommaSeperatedValue(value: String): String
    {
        var str = ""
        try
        {
            val formatter = DecimalFormat("#,##,###")
            str = ""
            str = formatter.format(java.lang.Double.parseDouble(value))
        }
        catch (e: NumberFormatException)
        {
            e.printStackTrace()
        }

        return str
    }

    fun getCoppiedText(context: Context, text: String)
    {
        var clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clip = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(clip)
    }


    fun showFullDialog(dialog: BottomSheetDialog)
    {
        dialog.setOnShowListener { dialog ->
            try
            {
                val d = dialog as BottomSheetDialog
                val bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }

    fun showToast(message: String, activity: Activity)
    {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun getFilePath(activity: Activity, pathUri: Uri): String?
    {
        var cursor: Cursor? = null
        try
        {
            val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
            cursor = activity.getContentResolver().query(pathUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            return cursor.getString(column_index)
        }
        catch (e: Exception)
        {
            Log.e("Tag", "getRealPathFromURI Exception : " + e.toString())
            return ""
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close()
            }
        }
    }

    fun getFileNameFromPath(activity: Activity, pathUri: Uri): String
    {
        var filename: String = ""
        var fileWOExtension: String = ""

        try
        {
            var path = getFilePath(activity, pathUri)

            filename = path!!.substring(path.lastIndexOf("/") + 1);

            fileWOExtension = ""

            if (filename.indexOf(".") > 0)
            {
                fileWOExtension = filename.substring(0, filename.lastIndexOf("."));
            }
            else
            {
                fileWOExtension = filename;
            }

            Log.d("TAG", "Real Path: " + path);
            Log.d("TAG", "Filename With Extension: " + filename);
            Log.d("TAG", "File Without Extension: " + fileWOExtension);
        }
        catch (e: Exception)
        {
        }

        return filename
    }



    fun saveImage(activity: Activity,bmp: Bitmap, thumbnailName : String) : String
    {
        var folderPath  = activity.getExternalFilesDir(null)!!.absolutePath + "/AlphaeStateVault/PDF/Thumbnail"
        var out: FileOutputStream? = null
        try
        {
            val folder = File(folderPath)
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "$thumbnailName.png")
            out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance

            return file.absolutePath
        }
        catch (e: Exception)
        {
            //todo with exception
            e.printStackTrace()
            return ""
        }
        finally
        {
            try
            {
                out?.close()
            }
            catch (e: Exception)
            {
                //todo with exception
                e.printStackTrace()
            }
        }
    }


}
