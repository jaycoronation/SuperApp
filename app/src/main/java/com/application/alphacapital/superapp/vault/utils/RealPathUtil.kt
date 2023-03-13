import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.*
import java.text.DecimalFormat
import java.util.*


object RealPathUtil
{
    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    fun getExtensionWithDot(uri: String?): String?
    {
        if (uri == null)
        {
            return null
        }
        val dot: Int = uri.lastIndexOf(".")
        return if (dot >= 0)
        {
            uri.substring(dot)
        }
        else
        {
            // No extension.
            ""
        }
    }

    /**
     * Gets the extension of a file name, like "png" or "jpg".
     *
     * @param filename
     * @return Extension excluding the dot("."); "" if there is no extension;
     */
    fun getExtension(fileName: String): String?
    {
        val namearr: Array<String> = fileName.split("\\.").toTypedArray()
        return namearr[namearr.size - 1]
    }


    /**
     * Gets the name of a file path, like "abc.png" or "abc.jpg".
     *
     * @param filePath
     * @return filename including the dot(".png");
     */
    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.getScheme() == "content") {
            val cursor: Cursor = context.getContentResolver().query(uri, null, null, null, null)!!
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor.close()
            }
        }
        if (result == null) {
            result = uri.getPath()
            val cut: Int = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }


    /**
     * @return Whether the URI is a local one.
     */
    fun isLocal(url: String?): Boolean
    {
        return if (url != null && !url.startsWith("http://") && !url.startsWith("https://"))
        {
            true
        }
        else false
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    fun isMediaUri(uri: Uri): Boolean
    {
        return "media".equals(uri.getAuthority(), ignoreCase = true)
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    fun getUri(file: File?): Uri?
    {
        return if (file != null)
        {
            Uri.fromFile(file)
        }
        else null
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    fun getPathWithoutFilename(file: File?): File?
    {
        return if (file != null)
        {
            if (file.isDirectory())
            { // no file to be split off. Return everything
                file
            }
            else
            {
                val filename: String = file.getName()
                val filepath: String = file.getAbsolutePath()
                // Construct path without file name.
                var pathwithoutname: String = filepath.substring(0,
                        filepath.length - filename.length)
                if (pathwithoutname.endsWith("/"))
                {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
                }
                File(pathwithoutname)
            }
        }
        else null
    }

    /**
     * @return The MIME type for the given file.
     */
    fun getMimeType(file: File): String?
    {
        val extension = getExtension(file.getName())
        return if (extension!!.length > 0) MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1)) else "application/octet-stream"
    }

    /**
     * @return The MIME type for the give Uri.
     */
    fun getMimeType(context: Context, uri: Uri): String?
    {
        val file = File(getRealPath(context, uri))
        return getMimeType(file)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is [LocalStorageProvider].
     * @author paulburke
     */
    fun isLocalStorageDocument(uri: Uri?): Boolean
    {
        return false //LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @see .getPath
     * @author paulburke
     */
    fun getFile(context: Context, uri: Uri): File?
    {
        if (uri != null)
        {
            val path = getRealPath(context, uri)
            if (path != null && isLocal(path))
            {
                return File(path)
            }
        }
        return null
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    fun getReadableFileSize(size: Int): String?
    {
        val BYTES_IN_KILOBYTES = 1024
        val dec = DecimalFormat("###.#")
        val KILOBYTES = " KB"
        val MEGABYTES = " MB"
        val GIGABYTES = " GB"
        var fileSize = 0f
        var suffix = KILOBYTES
        if (size > BYTES_IN_KILOBYTES)
        {
            fileSize = size / BYTES_IN_KILOBYTES.toFloat()
            if (fileSize > BYTES_IN_KILOBYTES)
            {
                fileSize = fileSize / BYTES_IN_KILOBYTES
                if (fileSize > BYTES_IN_KILOBYTES)
                {
                    fileSize = fileSize / BYTES_IN_KILOBYTES
                    suffix = GIGABYTES
                }
                else
                {
                    suffix = MEGABYTES
                }
            }
        }
        return (dec.format(fileSize.toDouble()) + suffix).toString()
    }

    /**
     * File and folder comparator. TODO Expose sorting option method
     *
     * @author paulburke
     */
    var sComparator: Comparator<File> = object : Comparator<File>
    {
        override fun compare(f1: File, f2: File): Int
        {
            // Sort alphabetically by lower case, which is much cleaner
            return f1.getName().toLowerCase().compareTo(
                    f2.getName().toLowerCase())
        }
    }

    /**
     * The pattern of setting up RSV to record can orphan file stubs for unused output files,
     * so we need to check and clean them up from time to time.
     */
    fun cleanUpFileStubs()
    {
        val path: String = (Environment.getExternalStorageDirectory().toString() + File.separator + "LipSwap"
                            + File.separator)
        val directory: File = File(path)
        val files: Array<File> = directory.listFiles()
        if (files != null)
        {
            for (i in files.indices)
            {
                if (files[i].length() == 0L)
                {
                    files[i].delete()
                }
            }
        }
    }


    fun getRealPath(context: Context, fileUri: Uri): String?
    {
        // SDK >= 11 && SDK < 19
        return if (Build.VERSION.SDK_INT < 19)
        {
            getRealPathFromURIAPI11to18(context, fileUri)
        }
        else
        {
            getRealPathFromURIAPI19(context, fileUri)
        }// SDK > 19 (Android 4.4) and up
    }

    @SuppressLint("NewApi")
    fun getRealPathFromURIAPI11to18(context: Context, contentUri: Uri): String?
    {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null)
        {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author Niks
     */
    @SuppressLint("NewApi")
    fun getRealPathFromURIAPI19(context: Context, uri: Uri): String?
    {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
            {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true))
                {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            }
            else if (isDownloadsDocument(uri))
            {
                var cursor: Cursor? = null
                try
                {
                    cursor = context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
                    cursor!!.moveToNext()
                    val fileName = cursor.getString(0)
                    val path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                    if (!TextUtils.isEmpty(path))
                    {
                        return path
                    }
                }
                finally
                {
                    cursor?.close()
                }
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:"))
                {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads"), java.lang.Long.valueOf(id))

                return getDataColumn(context, contentUri, null, null)
            }
            else if (isMediaDocument(uri))
            {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type)
                {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        }
        else if ("content".equals(uri.scheme!!, ignoreCase = true))
        {
            // Return the remote address
            if (isGooglePhotosUri(uri))
            {
                return uri.lastPathSegment
            }
            else if (isGoogleDriveUri(uri))
            {
                Log.e("Inside Google Drive","true" + "::::::::::" + uri.toString())
                val docId: String = DocumentsContract.getDocumentId(uri)
                val split: Array<String> = docId.split(";").toTypedArray()
                val acc = split[0]
                val doc = split[1]
                /*
                * @details google drive document data. - acc , docId.
                * */
                /*
                * @details google drive document data. - acc , docId.
                * */
                return saveFileIntoExternalStorageByUri(context, uri)?.toString()
            }

            return  getDataColumn(context, uri, null, null)
        }
        else if ("file".equals(uri.scheme!!, ignoreCase = true))
        {
            return uri.path
        }// File
        // MediaStore (and general)
        return null
    }

    @kotlin.jvm.Throws(Exception::class)
    fun saveFileIntoExternalStorageByUri(context: Context, uri: Uri): File? {
        val inputStream: InputStream = context.getContentResolver().openInputStream(uri)!!
        val originalSize: Int = inputStream.available()
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        val fileName = getFileName(context, uri)
        val file: File = makeEmptyFileIntoExternalStorageWithTitle(fileName)
        bis = BufferedInputStream(inputStream)
        bos = BufferedOutputStream(FileOutputStream(file, false))
        val buf = ByteArray(originalSize)
        bis.read(buf)
        do {
            bos.write(buf)
        } while (bis.read(buf) != -1)
        bos.flush()
        bos.close()
        bis.close()
        return file
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author Niks
     */
    private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                              selectionArgs: Array<String>?): String?
    {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try
        {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst())
            {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        }
        finally
        {
            cursor?.close()
        }
        return null
    }

    fun makeEmptyFileIntoExternalStorageWithTitle(title: String?): File {
        val root: String = Environment.getExternalStorageDirectory().getAbsolutePath()
        return File(root, title)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean
    {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean
    {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean
    {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean
    {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Drive.
     */
    private fun isGoogleDriveUri(uri: Uri): Boolean
    {
        return "com.google.android.apps.docs.storage" == uri.authority
    }
}
