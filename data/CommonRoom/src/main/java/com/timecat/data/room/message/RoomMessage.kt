package com.timecat.data.room.message

import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.BLOCK_MESSAGE
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description null
 * @usage null
 * \~chinese
 * 代表一条发送或接收到的消息
 *
 * <p>
 * 构造一条文本发送消息
 * <pre>
 *     RoomMessage msg = RoomMessage.createSendMessage(MessageType.TXT);
 *     msg.setTo("user1");
 *     TextMessageBody body = new TextMessageBody("hello from hyphenate sdk");
 *     msg.addBody(body);
 * </pre>
 *
 * <p>
 * 构造一条图片消息
 * <pre>
 *      RoomMessage msg = RoomMessage.createSendMessage(MessageType.IMAGE);
 *      msg.setTo("user1");
 *      ImageMessageBody body = new ImageMessageBody(imageFileUrl);
 *      msg.addBody(body);
 * </pre>
 *
 * \~english
 * represent a sent/recv message
 *<p>
 * construct a new send text message
 * <pre>
 *     RoomMessage msg = RoomMessage.createSendMessage(MessageType.TXT);
 *     msg.setTo("user1");
 *     TextMessageBody body = new TextMessageBody("hello from hyphenate sdk");
 *     msg.addBody(body);
 * </pre>
 *
 * <p>
 * construct a new recv text message
 * <pre>
 *      RoomMessage msg = RoomMessage.createSendMessage(MessageType.IMAGE);
 *      msg.setTo("user1");
 *      ImageMessageBody body = new ImageMessageBody(imageFileUrl);
 *      msg.addBody(body);
 * </pre>
 */


/**
 * \~chinese
 * 消息类型：文本，图片，视频，位置，语音，文件, 透传消息
 *
 * \~english
 * message type:TXT,IMAGE,VIDOE,LOCATION,VOICE,FILE,CMD
 */
enum class MessageType(val id: Int) {
    TXT(BLOCK_TEXT),
    IMAGE(BLOCK_IMAGE),
    VIDEO(BLOCK_VIDEO),
    LOCATION(BLOCK_LOCATION),
    VOICE(BLOCK_VOICE),
    FILE(BLOCK_FILE),
    CMD(BLOCK_CMD),
    CALL(BLOCK_CALL),
    BIG_EMOTION(BLOCK_BIG_EMOTION),
}

/**
 * \~chinese
 * 聊天类型：单聊，群聊，聊天室
 *
 * \~english
 * chat type : private chat, group chat, chat room
 */
typealias ChatType = RoomConversationType

/**
 * \~chinese
 * 消息的方向类型：区分是发送消息还是接收到的消息
 *
 * \~english
 * message direction,send direction or receive direction
 */
enum class Direct(val id: Int) {
    SEND(0), RECEIVE(1)
}

/**
 * \~chinese
 * 消息的发送/接收状态：成功，失败，发送/接收过程中，创建成功待发送
 *
 * \~english
 * message status
 */
enum class Status {
    SUCCESS, FAIL, INPROGRESS, CREATE
}


/**
 * \~chinese
 * 创建一个发送消息
 * @param messageType 消息类型
 * @return 消息对象
 *
 * \~english
 * create a new send message
 * @param messageType the message type
 * @return the message object
 */
fun createSendMessage(messageType: MessageType): RoomRecord {
    val record = RoomRecord.forName("")
    record.from = "me"
    record.to = ""
    record.msgType = messageType.id
    record.type = BLOCK_MESSAGE
    return record
}


/**
 * \~chinese
 * 创建一个接收消息
 * @param messageType 消息类型
 * @return 消息对象
 *
 * \~english
 * create a new recv message
 * @param messageType message type
 * @return the message object
 */
fun createReceiveMessage(messageType: MessageType): RoomRecord {
    val record = RoomRecord.forName("")
    record.from = ""
    record.to = "me"
    record.msgType = messageType.id
    record.type = BLOCK_MESSAGE
    return record
}

/**
 * \~chinese
 * 创建一个文本发送消息
 * @param content 文本内容
 * @param username 消息接收人或群id
 * @return
 *
 * \~english
 * @param content text content
 * @param username the recipient(user or group) id
 * @return
 */
fun createTxtSendMessage(content: String, username: String): RoomRecord? {
    if (content.isNotEmpty()) {
        val msg: RoomRecord = createSendMessage(MessageType.TXT)
        val txtBody = RoomTextMessageBody(content)
        msg.addBody(txtBody)
        msg.to = username
        return msg
    }
    Log.e(RoomRecord.TAG, "text content size must be greater than 0")
    return null
}

/**
 * \~chinese
 * 创建一个语音发送消息
 * @param filePath 语音文件路径
 * @param timeLength 语音时间长度(单位秒)
 * @param username 消息接收人或群id
 * @return
 *
 * \~english
 * create a voice send message
 * @param filePath the path of the voice file
 * @param timeLength the time length of the voice(unit s)
 * @param username  the recipient id
 * @return
 */
fun createVoiceSendMessage(
    filePath: String?,
    timeLength: Int,
    username: String
): RoomRecord? {
    if (!File(filePath).exists()) {
        Log.e(RoomRecord.TAG, "voice file does not exsit")
        return null
    }
    val record: RoomRecord = createSendMessage(MessageType.VOICE)
    // 如果是群聊，设置chattype,默认是单聊
    val body = RoomVoiceMessageBody(File(filePath), timeLength)
    record.addBody(body)
    record.to = username
    return record
}

/**
 * \~chinese
 * 创建一个图片发送消息
 * @param filePath 图片路径
 * @param sendOriginalImage 是否发送原图(默认大于100K的图片sdk会进行压缩)
 * @param username 消息接收人或群id
 * @return
 *
 * \~english
 * create a image send message
 * @param filePath the path of the image
 * @param sendOriginalImage whether to send the original(if image greater than 100k sdk will be compressed)
 * @param username the recipient id
 * @return
 */
fun createImageSendMessage(
    filePath: String?,
    username: String
): RoomRecord? {
    if (!File(filePath).exists()) {
        Log.e(RoomRecord.TAG, "image file does not exsit")
        return null
    }
    // create and add image message in view
    val record: RoomRecord = createSendMessage(MessageType.IMAGE)
    record.to = username
    val body = RoomImageMessageBody(File(filePath))
    record.addBody(body)
    return record
}

/**
 * \~chinese
 * 创建一个视频发送消息
 * @param videofilePath 视频文件路径
 * @param imageThumbPath 视频第一帧图缩略图
 * @param timeLength 视频时间长度(单位秒)
 * @param username 消息接收人或群id
 * @return
 *
 * \~english
 * create a video send message
 * @param videofilePath the path of the video file
 * @param imageThumbPath the path of the thumbnail
 * @param timeLength the length of the video time, unit s
 * @param username the recipient id
 * @return
 */
fun createVideoSendMessage(
    videofilePath: String,
    imageThumbPath: String?,
    timeLength: Int,
    username: String
): RoomRecord? {
    val videoFile = File(videofilePath)
    if (!videoFile.exists()) {
        Log.e(RoomRecord.TAG, "video file does not exist")
        return null
    }
    val record: RoomRecord = createSendMessage(MessageType.VIDEO)
    record.to = username
    val body = RoomVideoMessageBody(
        videofilePath, imageThumbPath, timeLength, videoFile.length()
    )
    record.addBody(body)
    return record
}

/**
 * \~chinese
 * 创建一个位置发送消息
 * @param latitude 纬度
 * @param longitude 经度
 * @param locationAddress 位置详情
 * @param username 消息接收人或群id
 * @return
 *
 * \~english
 * create a location send message
 * @param latitude the latitude
 * @param longitude the longitude
 * @param locationAddress location details
 * @param username the recipient id
 * @return
 */
fun createLocationSendMessage(
    latitude: Double,
    longitude: Double,
    locationAddress: String?,
    username: String
): RoomRecord? {
    val record: RoomRecord = createSendMessage(MessageType.LOCATION)
    val locBody = RoomLocationMessageBody(locationAddress, latitude, longitude)
    record.addBody(locBody)
    record.to = username
    return record
}

/**
 * \~chinese
 * 创建一个普通文件发送消息
 * @param filePath 文件路径
 * @param username 消息接收人或群id
 * @return
 *
 * \~english
 * create a normal file send message
 * @param filePath the path of the file
 * @param username the recipient id
 * @return
 */
fun createFileSendMessage(filePath: String, username: String): RoomRecord? {
    val file = File(filePath)
    if (!file.exists()) {
        Log.e(RoomRecord.TAG, "file does not exist")
        return null
    }
    // 创建一个文件消息
    val record: RoomRecord = createSendMessage(MessageType.FILE)
    record.to = username
    // add message body
    val body = RoomNormalFileMessageBody(File(filePath))
    record.addBody(body)
    return record
}


/**
 * \~chinese
 * 消息方向
 *
 * \~english
 * the message direction
 */
fun RoomRecord.direct(): Direct = when (direct) {
    Direct.SEND.id -> Direct.SEND
    else -> Direct.RECEIVE
}

/**
 * \~chinese
 * 设置消息的方向
 * @param dir
 *
 * \~english
 * set message direction
 * @param dir
 */
fun RoomRecord.setDirection(dir: Direct) {
    direct = dir.id
}

/**
 * \~chinese
 * 获取聊天类型
 * @return ChatType
 *
 * \~english
 * get chat type
 * @return ChatType
 */
fun RoomRecord.getChatType(): ChatType = when (msgChatType) {
    ChatType.Chat.id -> ChatType.Chat
    ChatType.GroupChat.id -> ChatType.GroupChat
    else -> ChatType.ChatRoom
}

/**
 * \~chinese
 * 设置聊天类型， 缺省为单聊  ChatType.Chat
 * @param chatType
 * @see ChatType
 *
 * \~english
 * set chat type, the default is single chat ChatType.Chat
 *
 * @param chatType
 * @see ChatType
 */
fun RoomRecord.setChatType(chatType: ChatType) {
    msgChatType = when (chatType) {
        ChatType.Chat -> ChatType.Chat.id
        ChatType.GroupChat -> ChatType.GroupChat.id
        else -> ChatType.ChatRoom.id
    }
}

/**
 * \chinese
 * 获取消息类型
 * @return
 *
 * \~english
 * get message chat type
 * @return
 */
fun RoomRecord.getType(): MessageType {
    when (msgType) {
        MessageType.TXT.id -> {
            return MessageType.TXT
        }
        MessageType.IMAGE.id -> {
            return MessageType.IMAGE
        }
        MessageType.CMD.id -> {
            return MessageType.CMD
        }
        MessageType.FILE.id -> {
            return MessageType.FILE
        }
        MessageType.VIDEO.id -> {
            return MessageType.VIDEO
        }
        MessageType.VOICE.id -> {
            return MessageType.VOICE
        }
        MessageType.LOCATION.id -> {
            return MessageType.LOCATION
        }
        MessageType.CALL.id -> {
            return MessageType.CALL
        }
        MessageType.BIG_EMOTION.id -> {
            return MessageType.BIG_EMOTION
        }
        else -> return MessageType.TXT
    }
}

/**
 * \~chinese
 * 添加消息体. （现在只支持添加一个 MesageBody)
 * @param body 消息体
 *
 * \~english
 * add a message body,only support add one now
 * @param body the message body
 */
fun RoomRecord.addBody(body: RoomMessageBody) {
    this.bodyStr = JSONObject.toJSONString(body)
}

fun RoomRecord.getBody(): RoomMessageBody {
    Log.e("getBody", bodyStr)
    return JSONObject.parseObject(
        bodyStr, when (msgType) {
            MessageType.TXT.id -> RoomTextMessageBody::class.java
            MessageType.IMAGE.id -> RoomImageMessageBody::class.java
            MessageType.VIDEO.id -> RoomVideoMessageBody::class.java
            MessageType.LOCATION.id -> RoomLocationMessageBody::class.java
            MessageType.VOICE.id -> RoomVoiceMessageBody::class.java
            MessageType.FILE.id -> RoomFileMessageBody::class.java
            MessageType.CMD.id -> RoomCmdMessageBody::class.java
            else -> RoomTextMessageBody::class.java
        }
    )
}


@Synchronized
fun RoomRecord.setInnerCallback(callback: RoomCallBack?) {
    val callbackHolder = messageStatusCallBack
    if (callbackHolder != null) {
        callbackHolder.innerCallback = callback
    } else {
        messageStatusCallBack = EMCallbackHolder(null)
        messageStatusCallBack!!.innerCallback = callback
    }
    setCallback(messageStatusCallBack)
}

/**
 * \~chinese
 * 设置消息状态改变的回调
 * @param callback
 *
 * \~english
 * set message status callback, your app should set emaObject callback to get message status and then refresh the ui accordingly
 * @param callback
 */
@Synchronized
fun RoomRecord.setMessageStatusCallback(callback: RoomCallBack?) {
    val callbackHolder = messageStatusCallBack
    if (callbackHolder != null) {
        callbackHolder.update(callback)
    } else {
        messageStatusCallBack = EMCallbackHolder(callback)
    }
    setCallback(messageStatusCallBack)
}

fun RoomRecord.setCallback(holder: EMCallbackHolder?) {
    roomCallBack = object : RoomCallBack {
        override fun onSuccess() {
            holder?.onSuccess()
        }

        override fun onProgress(progress: Int, status: String?) {
            holder?.onProgress(progress, status)
        }

        override fun onError(code: Int, error: String?) {
            holder?.onError(code, error)
        }
    }
}

fun RoomRecord.makeCallbackStrong() {
    val callbackHolder = messageStatusCallBack
    callbackHolder?.makeItStrong()
}

/**
 * \~chinese
 * 消息的id
 *
 * \~english
 * message id
 */
var RoomRecord.msgId: String
    get() = uuid
    set(value) {
        uuid = value
    }

/**
 * \~chinese
 * 消息发送者
 *
 * \~english
 * sender id
 */
var RoomRecord.from: String
    get() = extension.getString("from") ?: ""
    set(value) {
        extension.put("from", value)
    }//ext
/**
 * \~chinese
 * 消息的接收者
 *
 * \~english
 * receiver name
 */
var RoomRecord.to: String
    get() = extension.getString("to") ?: ""
    set(value) {
        extension.put("to", value)
    }//ext
/**
 * \~chinese
 * 消息的body
 *
 * \~english
 * message body
 */
var RoomRecord.bodyStr: String
    get() = content
    set(value) {
        content = value
    }

/**
 * \chinese
 * 消息类型
 *
 * \~english
 * message chat type
 */
var RoomRecord.msgType: Int
    get() = subType
    set(value) {
        subType = value
    }

/**
 * \~chinese
 * 聊天类型
 *
 * \~english
 * chat type
 */
var RoomRecord.msgChatType: Int
    get() = extension.getInteger("msgChatType") ?: 0
    set(value) {
        extension.put("msgChatType", value)
    }//ext
/**
 * \~chinese
 * 获取消息的本地接收时间
 * @return
 *
 * \~english
 * get local receive time
 * @return
 */
var RoomRecord.localTime: Long
    get() = extension.getLong("localTime") ?: 0L
    set(value) {
        extension.put("localTime", value)
    }//ext
/**
 * \~chinese
 * 消息的时间(server time)
 *
 * \~english
 * message time stamp(server time)
 */
var RoomRecord.msgTime: Long
    get() = updateTime
    set(value) {
        updateTime = value
    }//updateTime

/**
 * unread flag
 */
var RoomRecord.unread: Boolean
    get() = extension.getBoolean("unread")
    set(value) {
        extension.put("unread", value)
    }
//ext
/**
 * \~chinese
 * 语音是否已听
 *
 * \~english
 * whether the other has been listen
 */
var RoomRecord.isListened: Boolean
    get() = extension.getBoolean("isListened") ?: false
    set(value) {
        extension.put("isListened", value)
    }//ext

/**
 * \~chinese
 * 对方是否已读
 *
 * \~english
 * Read Ack
 */
var RoomRecord.acked: Boolean
    get() = extension.getBoolean("acked") ?: false
    set(value) {
        extension.put("acked", value)
    }//ext
/**
 * \~chinese
 * 消息方向
 *
 * \~english
 * the message direction
 */
var RoomRecord.direct: Int
    get() = extension.getInteger("direct") ?: 0
    set(value) {
        extension.put("direct", value)
    }//ext