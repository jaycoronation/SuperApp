package com.alphaestatevault.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import com.application.alphacapital.superapp.R

class ImageLoadingUtils(private val context: Context)
{
    var icon: Bitmap
    fun convertDipToPixels(dips: Float): Int
    {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, r.displayMetrics).toInt()
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int
    {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth)
        {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = width * height.toFloat()
        val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
        {
            inSampleSize++
        }
        return inSampleSize
    }

    fun decodeBitmapFromPath(filePath: String?): Bitmap?
    {
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        scaledBitmap = BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calculateInSampleSize(options, convertDipToPixels(150f), convertDipToPixels(200f))
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inJustDecodeBounds = false
        scaledBitmap = BitmapFactory.decodeFile(filePath, options)
        return scaledBitmap
    }

    init
    {
        icon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
    }
}
