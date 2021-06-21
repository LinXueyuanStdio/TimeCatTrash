package com.timecat.component.setting

import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.timecat.extend.arms.BaseApplication
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/22
 * @description 拓展函数
 * @usage null
 */
fun Uri.getFileName(): String {
    var fileName = PATH.random()
    val cr = BaseApplication.getContext().contentResolver
    val cursor = cr.query(this, null, null, null, null)
    if (cursor != null && cursor.count > 0) {
        cursor.moveToFirst()
        val col = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
        fileName = cursor.getString(col)
        cursor.close()
    }
    return fileName
}

fun Uri.copyToOver(fileName: String) {
    thread {
        val c = BaseApplication.getContext()
        val cr = c.contentResolver
        val inputStream = cr.openInputStream(this)
        val tempDir = PATH.over(dir = DIR.Temp)
        if (inputStream != null) {
            val file = tempDir.childFile(fileName)
            val fos = FileOutputStream(file)
            val bis = BufferedInputStream(inputStream)
            val bos = BufferedOutputStream(fos)
            val byteArray = ByteArray(1024)
            var bytes = bis.read(byteArray)
            while (bytes > 0) {
                bos.write(byteArray, 0, bytes)
                bos.flush()
                bytes = bis.read(byteArray)
            }
            bos.close()
            fos.close()
        }
    }
}

fun Uri.readAsDocument(context: Context = BaseApplication.getContext()): ParcelFileDescriptor? {
    return context.contentResolver.openFileDescriptor(this, "r")
}

fun String.childFile(fileName: String): File = File("$this/$fileName")

//读写权限
//Manifest.permission.WRITE_EXTERNAL_STORAGE
//Manifest.permission.READ_EXTERNAL_STORAGE

/**
 * 系统的文件选择器
 *
 * 1. 选择文件的类型
 * "application/pdf"
 *
 * 2. 返回 onActivityResult
 * when (requestCode) {
 *     PICK_FILE -> {
 *         if (resultCode == Activity.RESULT_OK && data != null) {
 *             val uri = data.data
 *             if (uri != null) {
 *                 val fileName = getFileNameByUri(uri)
 *                 copyUriToExternalFilesDir(uri, fileName)
 *             }
 *         }
 *     }
 * }
 */
fun Activity.pickFile(type: String = "*/*", reqCode: Int = 1) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = type
    startActivityForResult(intent, reqCode)
}

/**
 *
 * val bitmap = BitmapFactory.decodeResource(resources, R.drawable.image)
 * val displayName = "${System.currentTimeMillis()}.jpg"
 * val mimeType = "image/jpeg"
 * val compressFormat = Bitmap.CompressFormat.JPEG
 * addBitmapToAlbum(bitmap, displayName, mimeType, compressFormat)
 */
fun Context.addBitmapToAlbum(
    bitmap: Bitmap,
    displayName: String,
    mimeType: String,
    compressFormat: Bitmap.CompressFormat
): Uri? {
    val values = ContentValues()
    values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
    values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
    } else {
        values.put(
            MediaStore.MediaColumns.DATA,
            "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName"
        )
    }
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    if (uri != null) {
        val outputStream = contentResolver.openOutputStream(uri)
        if (outputStream != null) {
            bitmap.compress(compressFormat, 100, outputStream)
            outputStream.close()
        }
    }
    return uri
}

/**
 * val fileUrl = "http://guolin.tech/android.txt"
 * val fileName = "android.txt"
 * downloadFile(fileUrl, fileName)
 */
fun Context.downloadFile(fileUrl: String, fileName: String) {
    thread {
        try {
            val url = URL(fileUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            val inputStream = connection.inputStream
            val bis = BufferedInputStream(inputStream)
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                val outputStream = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    val bos = BufferedOutputStream(outputStream)
                    val buffer = ByteArray(1024)
                    var bytes = bis.read(buffer)
                    while (bytes >= 0) {
                        bos.write(buffer, 0, bytes)
                        bos.flush()
                        bytes = bis.read(buffer)
                    }
                    bos.close()
                }
            }
            bis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * Glide.with(context).load(uri).into(imageView)
 */
fun Context.loadImages(): ArrayList<Uri> {
    val imageList = ArrayList<Uri>()
    val cursor = contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null,
        null,
        null,
        "${MediaStore.MediaColumns.DATE_ADDED} desc"
    )
    if (cursor != null) {
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imageList.add(uri)
        }
        cursor.close()
    }
    return imageList
}