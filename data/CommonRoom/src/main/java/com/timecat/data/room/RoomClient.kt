package com.timecat.data.room

import com.timecat.extend.arms.BaseApplication
import com.timecat.component.setting.DEF
import com.timecat.data.room.message.*
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_CONVERSATION
import com.timecat.identity.data.block.type.BLOCK_Chat
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description null
 * @usage null
 */
object RoomClient {
    var caches: MutableMap<String, MessageCache> = Hashtable()

    @JvmStatic
    fun messageDao() = TimeCatRoomDatabase.forFile(BaseApplication.getContext()).recordDao()

    @JvmStatic
    fun conversationDao() =
        TimeCatRoomDatabase.forFile(BaseApplication.getContext()).recordDao()

    @JvmStatic
    fun recordDao() =
        TimeCatRoomDatabase.forFile(BaseApplication.getContext()).recordDao()

    @JvmStatic
    fun db() = TimeCatRoomDatabase.forFile(BaseApplication.getContext())

    fun sendMessage(message: RoomRecord) {
        messageDao().insert(message)
        message.messageStatusCallBack?.onProgress(20, "发送中")
        val c = conversationDao().get_BLOCK_CONVERSATION(message.parent)
        message.messageStatusCallBack?.onProgress(50, "发送中")
        c?.let {
            it.record = message
            it.msgCount += 1
            conversationDao().update(it)
            message.messageStatusCallBack?.onProgress(80, "发送中")
        }
        message.messageStatusCallBack?.onSuccess()

    }

    fun setVoiceMessageListened(record: RoomRecord) {
        record.messageStatusCallBack?.onProgress(50, "更新中")
        record.isListened = true
        messageDao().update(record)
        record.messageStatusCallBack?.onSuccess()
    }

    /**
     * \~chinese
     * 获取当前所有的会话
     *
     * \~english
     * get all conversations in the cache
     *
     * @return conversations which is a map with key&#45;&#62;conversation id : value&#45;&#62;RoomConversation
     */
    fun getConversationsList(): MutableList<RoomRecord> =
        conversationDao().getAll_BLOCK_CONVERSATION()

    /**
     * \~chinese
     * 获取会话，没有则返回null, 没找到则返回空
     * @param id user id or group id
     * @return 会话
     *
     * \~english
     * get conversation by id
     *
     * @param id user id, group id or chatroom id
     * @return EMConversation the existing conversation found by conversation, null if not found
     */
    fun getConversation(id: String): RoomRecord {
        val con = conversationDao().get_BLOCK_CONVERSATION(id)
        return when {
            con != null -> con
            else -> {
                val c = RoomRecord.forName("")
                c.type = BLOCK_CONVERSATION
                c.subType = BLOCK_Chat
                conversationDao().insert(c)
                c
            }
        }
    }

    fun getConversationForBook(bookUuid: String): RoomRecord {
        return conversationDao().getForBook_BLOCK_CONVERSATION(bookUuid)
            ?: getDefaultConversation()
    }

    fun getConversationsForBook(bookUuid: String): List<RoomRecord> {
        return conversationDao().getListForBook_BLOCK_CONVERSATION(bookUuid)
    }

    fun getDefaultConversation(): RoomRecord {
        val con = conversationDao().get_BLOCK_CONVERSATION(DEFAULT_CONVERSATION_ID)
        if (con == null) {
            val defaultConversation = RoomRecord.forBlock(DEFAULT_CONVERSATION_ID, "默认会话")
            defaultConversation.type = BLOCK_CONVERSATION
            defaultConversation.subType = BLOCK_Chat
            defaultConversation.content = "默认会话，系统级。"
            defaultConversation.coverImageUrl = "R.drawable.ic_comment"
            defaultConversation.setSystem(true)
            conversationDao().insert(defaultConversation)
            DEF.block().putString(DEFAULT_CONVERSATION_UUID, defaultConversation.uuid)
            return defaultConversation
        }
        return con
    }

    /**
     * \~chinese
     * 根据用户或群组id以及会话类型获取会话
     *
     * @param username 用户或群组id
     * @param createIfNotExists 没找到相应会话时是否自动创建
     * @return
     *
     * \~english
     * get conversation by conversation id and conversation type
     *
     * @param username user id, group id or chatroom id
     * @param createIfNotExists create conversation if not exists
     * @return
     */
    fun getConversation(
        id: String,
        createIfNotExists: Boolean
    ): RoomRecord? {
        val con = conversationDao().get_BLOCK_CONVERSATION(id)
        return when {
            con != null -> con
            !createIfNotExists -> null
            else -> {
                val c = RoomRecord.forName("")
                c.type = BLOCK_CONVERSATION
                c.subType = BLOCK_Chat
                c.parent = "me"
                conversationDao().insert(c)
                c
            }
        }
    }

    fun loadMoreMsgFromDB(
        conversationId: String,
        createTime: Long,
        pageSize: Int
    ): List<RoomRecord> {
        return messageDao().getAll_BLOCK_MESSAGE(conversationId, createTime, pageSize)
    }

    fun markAllMessagesAsRead(conversationId: String) {
        val messages: MutableList<RoomRecord> = messageDao().getAll_BLOCK_MESSAGE(conversationId)
        for (i in messages) {
            i.unread = false
            messageDao().updateMessages(i)
        }
    }

    fun markMessageAsRead(msgId: String, read: Boolean) {
        val m = getMessage(msgId) ?: return
        m.unread = !read
        messageDao().update(m)
    }

    /**
     * \~chinese
     * 获取指定ID 的消息对象
     *
     * @param messageId        消息ID
     * @return
     *
     * \~english
     * get message through message id
     *
     * @param messageId    message id
     * @return
     */
    fun getMessage(messageId: String): RoomRecord? {
        synchronized(caches) {
            for (cache in caches.values) {
                val msg: RoomRecord? = cache.getMessage(messageId)
                if (msg != null) {
                    return msg
                }
            }
        }
        return messageDao().get(messageId)
    }

    /**
     * \~chinese
     * 删除和指定用户或者群聊的对话(包括删除本地的聊天记录)
     *
     * @param id             用户名或者群聊id
     * @param deleteMessages 是否删除消息
     * @return               删除失败或者不存在此user的conversation返回false
     *
     * \~english
     * delete conversation and messages from local database
     *
     * @param id             user id or group id
     * @param deleteMessages whether delete messages
     * @return               return true if delete successfully
     */
    fun deleteConversation(
        id: String,
        deleteMessages: Boolean
    ): Boolean {
        val c = conversationDao().get_BLOCK_CONVERSATION(id)
        c?.let {
            val msgs = messageDao().getAll_BLOCK_MESSAGE(it.uuid)
            if (deleteMessages) {
                for (i in msgs)
                    messageDao().delete(i)
            }
            conversationDao().delete(it)
            return@deleteConversation true
        }
        return false
    }
}