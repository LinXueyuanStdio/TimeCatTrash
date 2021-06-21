package com.timecat.data.room.message

import com.timecat.data.room.record.RoomRecord
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-18
 * @description null
 * @usage null
 */
class MessageCache {
    var sortedMessages: TreeMap<Long, Any> = TreeMap(MessageComparator())
    var messages: MutableMap<String, RoomRecord> = HashMap()
    var idTimeMap: MutableMap<String, Long> = HashMap()
    var hasDuplicateTime = false
    val sortByServerTime: Boolean = true

    internal inner class MessageComparator : Comparator<Long> {
        override fun compare(time0: Long, time1: Long): Int {
            val `val` = time0 - time1
            return if (`val` > 0) {
                1
            } else if (`val` == 0L) {
                0
            } else {
                -1
            }
        }
    }

    @Synchronized
    fun getMessage(msgId: String?): RoomRecord? {
        return if (msgId == null || msgId.isEmpty()) {
            null
        } else messages[msgId]
    }

    @Synchronized
    fun addMessages(msgs: List<RoomRecord>) {
        for (msg in msgs) {
            addMessage(msg)
        }
    }

    @Synchronized
    fun addMessage(msg: RoomRecord) {
        if (msg.msgTime == 0L
            || msg.msgTime == -1L
            || msg.msgId.isEmpty()
            || msg.getType() == MessageType.CMD
        ) {
            return
        }
        val id: String = msg.msgId
        // override message
        if (messages.containsKey(id)) {
            val time = idTimeMap[id]!!
            sortedMessages.remove(time)
            messages.remove(id)
            idTimeMap.remove(id)
        }
        // messages share same time stamp
        val time: Long = if (sortByServerTime) msg.msgTime else msg.localTime
        if (sortedMessages.containsKey(time)) {
            hasDuplicateTime = true
            val v = sortedMessages[time]
            if (v != null) {
                if (v is RoomRecord) {
                    val msgs: MutableList<RoomRecord> = LinkedList()
                    msgs.add(v)
                    msgs.add(msg)
                    sortedMessages[time] = msgs
                } else if (v is List<*>) {
                    val msgs: MutableList<RoomRecord> = v as MutableList<RoomRecord>
                    msgs.add(msg)
                }
            }
        } else {
            sortedMessages[time] = msg
        }
        messages[id] = msg
        idTimeMap[id] = time
    }

    @Synchronized
    fun removeMessage(msgId: String) {
        if (msgId.isEmpty()) {
            return
        }
        val msg: RoomRecord? = messages[msgId]
        if (msg != null) {
            val time = idTimeMap[msgId]
            if (time != null) {
                if (hasDuplicateTime && sortedMessages.containsKey(time)) {
                    val v = sortedMessages[time]
                    if (v != null && v is List<*>) {
                        val msgs: MutableList<RoomRecord> = v as MutableList<RoomRecord>
                        for (m in msgs) {
                            if (m.msgId == msgId) {
                                msgs.remove(m)
                                break
                            }
                        }
                    } else {
                        sortedMessages.remove(time)
                    }
                } else {
                    sortedMessages.remove(time)
                }
                idTimeMap.remove(msgId)
            }
            messages.remove(msgId)
        }
    }

    @get:Synchronized
    val allMessages: List<RoomRecord>
        get() {
            val list: MutableList<RoomRecord> = ArrayList<RoomRecord>()
            if (!hasDuplicateTime) {
                for (v in sortedMessages.values) {
                    list.add(v as RoomRecord)
                }
            } else {
                for (v in sortedMessages.values) {
                    if (v is List<*>) {
                        list.addAll((v as List<RoomRecord>))
                    } else {
                        list.add(v as RoomRecord)
                    }
                }
            }
            return list
        }

    @get:Synchronized
    val lastRecord: RoomRecord?
        get() {
            if (sortedMessages.isEmpty()) {
                return null
            }
            val o = sortedMessages.lastEntry().value ?: return null
            if (o is RoomRecord) {
                return o as RoomRecord
            } else if (o is List<*>) {
                val msgs: List<RoomRecord> = o as List<RoomRecord>
                return if (msgs.isNotEmpty()) {
                    msgs[msgs.size - 1]
                } else null
            }
            return null
        }

    @Synchronized
    fun clear() { //no need to keep the last message
        sortedMessages.clear()
        messages.clear()
        idTimeMap.clear()
    }

    @get:Synchronized
    val isEmpty: Boolean
        get() = sortedMessages.isEmpty()
}