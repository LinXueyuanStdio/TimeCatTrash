package com.timecat.data.room.message

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-29
 * @description null
 * @usage null
 */
class RoomTextMessageBody(
    /**
     * \~chinese
     * 消息内容
     *
     * \~english
     *
     * text content
     */
    var msg: String
) : RoomMessageBody(EMAMessageBodyType_TEXT) {

    constructor() : this("")

    /**
     * \~chinese
     * 获取文本消息内容
     *
     * @return
     *
     * \~english
     * Text message body
     *
     * @return
     */
    fun getMessage(): String = msg

    override fun toString(): String {
        return "txt:\"$msg\""
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(msg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomTextMessageBody> {
        override fun createFromParcel(parcel: Parcel): RoomTextMessageBody {
            return RoomTextMessageBody(parcel)
        }

        override fun newArray(size: Int): Array<RoomTextMessageBody?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * \~chinese
 * 位置信息消息
 *
 * EMLocationMessageBody body = new EMLocationMessageBody(“地址”, 30.010378, 104.358878);
 *
 * @version 3.0
 *
 * \~english
 * location message body.
 *
 * EMLocationMessageBody body = new EMLocationMessageBody(“地址”, 30.010378, 104.358878);
 *
 */
class RoomLocationMessageBody(
    /**
     * \~chinese
     * 地址
     *
     * \~english
     * address
     */
    var address: String?,
    /**
     * \~chinese
     * 纬度
     *
     * \~english
     * latitude
     */
    var latitude: Double,
    /**
     * \~chinese
     * 经度
     *
     * \~english
     * longitude
     */
    var longitude: Double
) : RoomMessageBody(EMAMessageBodyType_LOCATION) {
    constructor() : this("", 0.0, 0.0)

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(address)
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomLocationMessageBody> {
        override fun createFromParcel(parcel: Parcel): RoomLocationMessageBody {
            return RoomLocationMessageBody(parcel)
        }

        override fun newArray(size: Int): Array<RoomLocationMessageBody?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * \~chinese
 * 创建一个命令消息
 *
 * RoomCmdMessageBody body = new RoomCmdMessageBody("delete");
 *
 * @version 2.0
 *
 * \~english
 * create a command message
 *
 * RoomCmdMessageBody body = new RoomCmdMessageBody("delete");
 *
 */
class RoomCmdMessageBody(
    /**
     * \~chinese
     * 创建一个命令消息
     *
     * @param action cmd指令
     *
     * \~english
     * construct EMCmdMessageBody
     *
     * @param action command
     */
    var action: String
) : RoomMessageBody(EMAMessageBodyType_COMMAND) {
    constructor() : this("")

    /**
     * \~chinese
     * 获取action
     *
     * @return EMCallManager
     *
     * \~english
     * get the action
     *
     * @return the action
     */
    fun action(): String? = action

    override fun toString(): String {
        return "RoomCmdMessageBody(action='$action')"
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(action)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomCmdMessageBody> {
        override fun createFromParcel(parcel: Parcel): RoomCmdMessageBody {
            return RoomCmdMessageBody(parcel)
        }

        override fun newArray(size: Int): Array<RoomCmdMessageBody?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * \~chinese
 * 普通文件消息体
 *
 * \~english
 * the normal file messag body class
 */
class RoomNormalFileMessageBody(
    override var localPath: String?
) : RoomFileMessageBody(localPath, EMAMessageBodyType_FILE) {
    constructor() : this("")

    /**
     * \~chinese
     * 获取文件大小
     * @return
     *
     * \~english
     * get the video file size
     * @return
     */
    var fileSize: Long = 0L

    /**
     * \~chinese
     * 构造一个文件消息体
     * @param file
     *
     * \~english
     * create a normal file messag body
     * @param file
     */
    constructor(file: File) : this(file.absolutePath)

    override fun toString(): String {
        return "normal file:localUrl:$localPath,file size:$fileSize"
    }

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(localPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomNormalFileMessageBody> {
        override fun createFromParcel(parcel: Parcel): RoomNormalFileMessageBody {
            return RoomNormalFileMessageBody(parcel)
        }

        override fun newArray(size: Int): Array<RoomNormalFileMessageBody?> {
            return arrayOfNulls(size)
        }
    }

}

/**
 * \~chinese
 * 图片消息体
 *
 * RoomImageMessageBody body = new RoomImageMessageBody(imageFile);
 *
 * \~english
 * the image message body class
 *
 * RoomImageMessageBody body = new RoomImageMessageBody(imageFile);
 *
 */
class RoomImageMessageBody(
    override var localPath: String?
) : RoomFileMessageBody(localPath, EMAMessageBodyType_IMAGE) {
    /**
     * \~chinese
     * 获取图片的宽度
     * @return
     *
     * \~english
     * get image width
     * @return
     */
    var width: Int = 0
    /**
     * \~chinese
     * 获取图片的高度
     * @return
     *
     * \~english
     * get image height
     * @return
     */
    var height: Int = 0

    constructor() : this("")

    /**
     * \~chinese
     * 创建一个图片消息体
     * @param imageFile 图片文件
     *
     * \~english
     * create a image message body
     *
     * @param imageFile the image file
     */
    constructor(imageFile: File) : this(imageFile.absolutePath) {
        val bitmap: Bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        this.width = bitmap.width
        this.height = bitmap.height
    }

    override fun toString(): String {
        return "localurl: " + getLocalUrl()
    }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(localPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomImageMessageBody> {
        override fun createFromParcel(parcel: Parcel): RoomImageMessageBody {
            return RoomImageMessageBody(parcel)
        }

        override fun newArray(size: Int): Array<RoomImageMessageBody?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * \~chinese
 * 视频消息体
 *
 * RoomVideoMessageBody body = new RoomVideoMessageBody(videoFilePath,thumbPath, duration, filelength);
 *
 * \~english
 * Video message body
 *
 * RoomVideoMessageBody body = new RoomVideoMessageBody(videoFilePath, thumbPath, duration, filelength);
 */
class RoomVideoMessageBody(
    /**
     * \~chinese
     * 视频文件路径
     *
     * \~english
     * the path of the video file
     *
     */
    var videoFilePath: String,
    /**
     * \~chinese
     * 预览图路径
     *
     * \~english
     * the path of the video first frame
     *
     */
    var thumbPath: String?,

    /**
     * \~chinese
     * 视频时长, 秒为单位
     *
     * \~english
     * video duration, in seconds
     *
     */
    var duration: Int,
    /**
     * \~chinese
     * 视频文件长度
     *
     * \~english
     * the length of the video file
     *
     */
    var filelength: Long
) : RoomFileMessageBody(videoFilePath, EMAMessageBodyType_VIDEO) {

    constructor() : this("", "", 0, 0)

    fun setThumbnailSize(width: Int, height: Int) {
    }

    /**
     * \~chinese
     * 获取视频缩略图的宽度
     * @return
     *
     * \~english
     * get video thumbnail width
     * @return
     */
    fun getThumbnailWidth(): Int = 0

    /**
     * \~chinese
     * 获取视频缩略图的高度
     * @return
     *
     * \~english
     * get video thumbnail height
     * @return
     */
    fun getThumbnailHeight(): Int = 0

    /**
     * \~chinese
     * 获取缩略图的本地路径
     *
     * UIImage *image = [UIImage imageWithContentsOfFile:thumbnailLocalPath];
     * @return
     *
     * \~english
     * Local path of thumbnail
     *
     * UIImage *image = [UIImage imageWithContentsOfFile:thumbnailLocalPath];
     *
     * @return
     */
    fun getLocalThumb(): String? = thumbPath

    fun setLocalThumb(localThumbPath: String?) {
        thumbPath = localThumbPath
    }


    override fun toString(): String {
        return "RoomVideoMessageBody(videoFilePath='$videoFilePath', thumbPath=$thumbPath, duration=$duration, filelength=$filelength)"
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.readLong()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(videoFilePath)
        dest.writeString(thumbPath)
        dest.writeInt(duration)
        dest.writeLong(filelength)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<RoomVideoMessageBody> {
        override fun createFromParcel(parcel: Parcel): RoomVideoMessageBody {
            return RoomVideoMessageBody(parcel)
        }

        override fun newArray(size: Int): Array<RoomVideoMessageBody?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * \~chinese
 * 语音消息体
 *
 * RoomVoiceMessageBody body = new RoomVoiceMessageBody(voiceFile, length);
 *
 * \~english
 * the voice message body class
 *
 * RoomVoiceMessageBody body = new RoomVoiceMessageBody(voiceFile, length);
 */
class RoomVoiceMessageBody(
    override var localPath: String?,
    /**
     * \~chinese
     * 获得语音文件的长度 （单位是秒）
     *
     * @return
     *
     * \~english
     * get the length of the voice time, unit s
     *
     * @return
     */
    var length: Long
) : RoomFileMessageBody(localPath, EMAMessageBodyType_VOICE) {

    constructor() : this("", 0)

    /**
     * \~chinese
     * 创建一个语音消息体
     *
     * @param voiceFile 语音文件路径
     * @param duration, 语音事件长度，单位秒
     *
     * \~english
     * create a voice message body
     *
     * @param voiceFile voice file
     * @param duration voice clip length, in seconds
     */
    constructor(
        voiceFile: File,
        duration: Int
    ) : this(voiceFile.absolutePath, duration.toLong()) {
        Log.d("voicemsg", "create voice, message body for:" + voiceFile.absolutePath)
    }

    override fun toString(): String {
        return ("voice:localPath:$localPath,length:$length")
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(localPath)
        dest.writeLong(length)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomImageMessageBody> {
        override fun createFromParcel(parcel: Parcel): RoomImageMessageBody {
            return RoomImageMessageBody(parcel)
        }

        override fun newArray(size: Int): Array<RoomImageMessageBody?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * \~chinese
 * 文件类消息的基类
 *
 * \~english
 * the base class of file type message
 */
open abstract class RoomFileMessageBody(
    open var localPath: String?,
    override var type: Int
) : RoomMessageBody(type) {

    enum class RoomDownloadStatus {
        DOWNLOADING, SUCCESSED, FAILED, PENDING
    }

    /**
     * \~chinese
     * 获得文件名
     *
     * @return
     *
     * \~english
     * get file name
     * @return
     */
    open fun getFileName(): String? = localPath

    open fun setFileName(fileName: String?) {
        localPath = fileName
    }

    /**
     * \~chinese
     * 本地图片，语音等文件的路径
     *
     * @return
     *
     * \~english
     * local file path
     */
    open fun getLocalUrl(): String? = localPath

    open fun setLocalUrl(localUrl: String?) {
        localPath = localUrl
    }

    open fun setSecret(secret: String?) {
        localPath = secret
    }

    open fun getSecret(): String? = localPath

    open fun displayName(): String? = localPath

    open fun setFileLength(length: Long) {
        localPath!!.length
    }
}

open abstract class RoomMessageBody(
    open var type: Int = EMAMessageBodyType_TEXT
) : Parcelable {
    companion object {
        const val EMAMessageBodyType_TEXT = 0
        const val EMAMessageBodyType_IMAGE = 1
        const val EMAMessageBodyType_VIDEO = 2
        const val EMAMessageBodyType_LOCATION = 3
        const val EMAMessageBodyType_VOICE = 4
        const val EMAMessageBodyType_FILE = 5
        const val EMAMessageBodyType_COMMAND = 6
    }
}
