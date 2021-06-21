package com.timecat.data.room.message

import com.timecat.data.room.RoomClient
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-29
 * @description null
 * @usage null
 */

/**
 * \~chinese
 * 获取会话类型
 *
 * \~english
 * get conversation type
 */
fun RoomRecord.getConversationType(): RoomConversationType {
    for (i in RoomConversationType.values()) {
        if (i.id == subType) {
            return i
        }
    }
    return RoomConversationType.Chat
}

/**
 * \~chinese
 * 将所有未读消息设置为已读
 *
 * \~english
 * Mark all messages as read
 *
 */
fun RoomRecord.markAllMessagesAsRead() = RoomClient.markAllMessagesAsRead(uuid)

//------------------------------------------------------------------------------------------
//-------------------------------------------待实现-----------------------------------------
//------------------------------------------------------------------------------------------
/**
 * \~chinese
 * 根据传入的参数从db加载startMsgId之前(存储顺序)指定数量的message，
 * 加载到的messages会加入到当前conversation的messages里
 *
 * @param startCreateTime    加载这个startCreateTime之前的message
 * @param pageSize      加载多少条
 * @return              消息列表
 *
 * \~english
 * load message from database, message id starting from param startMsgId, messages will also store in to memory cache
 * so next time calling getAllMessages(), result will contain those messages
 *
 * @param startCreateTime    message storage time will before this startCreateTime, if startMsgId is "" or null, will load last messages in database.
 * @param pageSize      message count to be loaded.
 * @return              messages
 */
fun RoomRecord.loadMoreMsgFromDB(
    startCreateTime: Long,
    pageSize: Int
): List<RoomRecord> {
    val msgs: List<RoomRecord> = RoomClient.loadMoreMsgFromDB(uuid, startCreateTime, pageSize)
    getCache().addMessages(msgs)
    return msgs
}

/**
 * \~chinese
 * 根据msgid获取消息
 *
 * @param  messageId    需要获取的消息id
 * @param  markAsRead   是否获取消息的同时标记消息为已读
 * @return              获取到的message实例
 *
 * \~english
 * get message by message ID, if the message already loaded into memory cache, will directly return the message, otherwise load message from database, and put it into cache.
 *
 * @param  messageId
 * @param  markAsRead    if true, will mark the message as read and send read ack to server
 * @return message
 */
fun RoomRecord.getMessage(messageId: String, markAsRead: Boolean): RoomRecord? {
    var msg: RoomRecord? = getCache().getMessage(messageId)
    if (msg == null) {
        msg = RoomClient.getMessage(messageId) ?: return null
    }
    RoomClient.markMessageAsRead(messageId, markAsRead)
    return msg
}

/**
 * \~chinese
 * 设置某条消息为已读
 *
 * @param messageId 消息ID
 *
 * \~english
 * mark the message as read, and send read as to server.
 *
 * @param messageId     messageID
 */
fun RoomRecord.markMessageAsRead(messageId: String) {
    val msg = RoomClient.getMessage(messageId)
    msg?.let {
        msg.unread = false
        RoomClient.messageDao().update(msg)
    }
}

/**
 * \~chinese
 * 获取此conversation当前内存所有的message。如果内存中为空，再从db中加载。
 *
 * @return
 *
 * \~english
 * get all message in local cache, if empty, then get from local database.
 */
fun RoomRecord.getAllMessages(): List<RoomRecord> {
    if (getCache().isEmpty) {
        val lastMsg = RoomClient.messageDao().getAll_BLOCK_MESSAGE(uuid)
        getCache().addMessages(lastMsg)
    }
    return getCache().allMessages
}

/**
 * \~chinese
 * 删除一条指定的消息
 *
 * @param messageId     待删除消息的ID
 *
 * \~english
 * delete a message
 *
 * @param messageId     message to be deleted
 */
fun RoomRecord.removeMessage(messageId: String) {
    val msg = RoomClient.getMessage(messageId)
    msg?.let {
        RoomClient.messageDao().delete(it)
    }
    getCache().removeMessage(messageId)
}

/**
 * \~chinese
 * 获取队列中的最后一条消息 （此操作不会改变未读消息计数）
 *
 * @return
 *
 * \~english
 * get last message from conversation
 */
fun RoomRecord.getLastMessage(): RoomRecord? {
    return if (getCache().isEmpty) {
        val msg = record ?: return null
        getCache().addMessage(msg)
        msg
    } else {
        getCache().lastRecord
    }
}

/**
 * \~chinese
 * 清除对话中的所有消息，只清除内存的，不清除db的消息
 * 在退出会话的时候清除内存缓存，减小内存消耗
 *
 * \~english
 * clear messages in this conversation's from cache, but will NOT clear local database
 */
fun RoomRecord.clear() {
    getCache().clear()
}

/**
 * \~chinese
 * 删除该会话所有消息，同时清除内存和数据库中的消息
 *
 * \~english
 * Delete all messages of the conversation from memory cache and local database
 */
fun RoomRecord.clearAllMessages() {
    getCache().clear()
    record = null
    msgCount = 0
    unreadCount = 0
    unread = false
    RoomClient.conversationDao().update(this)
    val messages = RoomClient.conversationDao().getAll_BLOCK_MESSAGE(uuid)
    messages.forEach {
        it.setDeleted(true)
        RoomClient.conversationDao().updateMessages(it)
    }
}


/**
 * \~chinese
 * 群组和聊天室类型都会返回true
 *
 * @return 群组和聊天室类型都会返回true, 其他类型返回false.
 *
 * \~english
 * group chat and chatroom chat will both return true
 *
 * @return group chat and chatroom chat will both return true, otherwise return false.
 */
fun RoomRecord.isGroup(): Boolean {
    val type = getConversationType()
    return type == RoomConversationType.GroupChat ||
            type == RoomConversationType.ChatRoom
}

/**
 * \~chinese
 * 插入一条消息，消息的conversationId应该和会话的conversationId一致，消息会被插入DB，并且更新会话的latestMessage等属性
 *
 * @param msg 消息实例
 *
 * \~english
 * Insert a message to a conversation in local database.
 * ConversationId of the message should be the same as conversationId of the conversation in order to insert the message into the conversation correctly.
 * The inserting message will be inserted based on timestamp.
 *
 * @param msg Message
 */
fun RoomRecord.insertMessage(msg: RoomRecord) {
    getCache().addMessage(msg)
    updateMessage(msg)
}

/**
 * \~chinese
 * 插入一条消息到会话尾部，消息的conversationId应该和会话的conversationId一致，消息会被插入DB，并且更新会话的latestMessage等属性
 *
 * @param msg 消息实例
 *
 * \~english
 * Insert a message to the end of a conversation in local database.
 * ConversationId of the message should be the same as conversationId of the conversation in order to insert the message into the conversation correctly.
 *
 * @param msg Message
 */
fun RoomRecord.appendMessage(msg: RoomRecord) {
    getCache().addMessage(msg)

}

/**
 * \~chinese
 * 更新本地的消息，不能更新消息ID，消息更新后，会话的latestMessage等属性进行相应更新
 *
 * @param msg 要更新的消息
 *
 * \~english
 * Update a message in local database. latestMessage of the conversation and other properties will be updated accordingly. messageId of the message cannot be updated
 *
 * @param msg Message
 */
fun RoomRecord.updateMessage(msg: RoomRecord): Boolean {
    record = msg
    msgCount += 1
    RoomClient.conversationDao().update(this)
    return true
}


// ====================================== Message cache ======================================

// ====================================== Message cache ======================================
fun RoomRecord.getCache(): MessageCache {
    var cache: MessageCache?
    synchronized(RoomClient.caches) {
        cache = RoomClient.caches[uuid]
        if (cache == null) {
            cache = MessageCache()
        }
        RoomClient.caches.put(uuid, cache!!)
    }
    return cache!!
}

/**
 * \~chinese
 * 会话类型: 包含单聊，群聊，聊天室，讨论组(暂时不可用)，客服
 *
 * \~english
 * Conversation type
 */
enum class RoomConversationType(val id: Int) {
    /**
     * \~chinese
     * 单聊会话
     *
     * \~english
     * one-to-one chat
     */
    Chat(BLOCK_Chat),
    /**
     * \~chinese
     * 群聊会话
     *
     * \~english
     * group chat
     */
    GroupChat(BLOCK_GroupChat),
    /**
     * \~chinese
     * 聊天室会话
     * \~english
     * chat room
     */
    ChatRoom(BLOCK_ChatRoom),
    /**
     * \~chinese
     * 目前没有使用
     * \~english
     * Not current used
     */
    DiscussionGroup(BLOCK_DiscussionGroup),
    HelpDesk(BLOCK_HelpDesk),
    SettingTabs(BLOCK_SettingTabs),
    Terminal(BLOCK_Terminal),
    Github(BLOCK_Github),
    Git(BLOCK_Git),
    Skin(BLOCK_Skin),
    Plugin(BLOCK_Plugin),
    Record(BLOCK_Record),
    Novel(BLOCK_NOVEL)
}

var RoomRecord.timestamp: Long
    get() = createTime
    set(value) {
        createTime = value
    }
var RoomRecord.draft: String
    get() = extension.getString("draft") ?: ""
    set(value) {
        extension.put("draft", value)
    }
var RoomRecord.msgCount: Int
    get() = extension.getInteger("msgCount") ?: 0
    set(value) {
        extension.put("msgCount", value)
    }
var RoomRecord.unreadCount: Int
    get() = extension.getInteger("unreadCount") ?: 0
    set(value) {
        extension.put("unreadCount", value)
    }
var RoomRecord.isTop: Boolean
    get() = isPined()
    set(value) {
        setPined(value)
    }
var RoomRecord.record: RoomRecord?
    get() {
        val json = extension.getString("record") ?: ""
        if (json.isNotBlank()) {
            return RoomRecord.fromJson(json)
        }
        return null
    }
    set(value) {
        extension.put("record", value?.toJson())
    }

