package com.timecat.component.setting.sound

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/26
 * @description null
 * @usage null
 */
object SoundAgreement {

    @JvmStatic
    fun getPath(context: Context, uri: Uri): String? {
        val wholeID = DocumentsContract.getDocumentId(uri)
        val id = wholeID.split(":".toRegex()).toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        val sel = MediaStore.Images.Media._ID + "=?"
        try {
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
            )?.use { cursor ->
                val columnIndex = cursor.getColumnIndex(column[0])
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            }
        } catch (ignored: Exception) {
        }
        return null
    }

    /**
     * 自己处理为空的情况，因为可能要用到 R.string.xxx
     */
    @JvmStatic
    fun getRingtoneName(context: Context, uri: Uri): String {
        val ringtone = RingtoneManager.getRingtone(context, uri)
        if (ringtone != null) {
            return ringtone.getTitle(context)
        } else {
            try {
                context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Audio.Media.TITLE),
                    MediaStore.Audio.Media._ID + " =?",
                    arrayOf(uri.lastPathSegment),
                    null
                )?.use { cur ->
                    val title = cur.getString(1)
                    if (title.isEmpty()) {
                        return cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    }
                }
            } catch (ignored: Exception) {
            }
        }
        return ""
    }

    @JvmStatic
    fun getSounds(context: Context, defaultValue: String?): ArrayList<SoundModel> {
        val notificationSounds = ArrayList<SoundModel>()
        val ringtoneManager = RingtoneManager(context)
        ringtoneManager.setType(RingtoneManager.TYPE_NOTIFICATION)
        ringtoneManager.cursor?.use { ringsCursor ->
            while (ringsCursor.moveToNext()) {
                val title = ringsCursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                val path = ringsCursor.getString(RingtoneManager.URI_COLUMN_INDEX)
                val id = ringsCursor.getString(RingtoneManager.ID_COLUMN_INDEX)
                val uri = Uri.parse("$path/$id")
                val selected = defaultValue != null
                        && (uri.toString().contains(defaultValue)
                        || title.equals(defaultValue, ignoreCase = true)
                        || defaultValue.contains(title))
                notificationSounds.add(SoundModel(title, uri, selected))
            }
        }
        return notificationSounds
    }

    /**
     * 转换为适合存储的数据类型
     */
    @JvmStatic
    fun toSaveData(uri: Uri) = uri.toString()

    /**
     * 转换为适合使用的数据类型
     */
    @JvmStatic
    fun toUsingData(str: String): Uri? = Uri.parse(str)

    @JvmStatic
    fun defaultSaveType(context: Context): String = toSaveData(defaultSound(context))

    @JvmStatic
    fun toSoundModel(uri: Uri, all: List<SoundModel>): SoundModel? = all.first { it.uri == uri }

    @JvmStatic
    fun toSoundModel(context: Context, uri: Uri): SoundModel =
        SoundModel(getRingtoneName(context, uri), uri, false)

    @JvmStatic
    fun defaultSound(context: Context): Uri {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (uri == null) {
            val rm = RingtoneManager(context)
            rm.setType(RingtoneManager.TYPE_NOTIFICATION)
            return rm.getRingtoneUri(0)
        }
        return uri
    }

}