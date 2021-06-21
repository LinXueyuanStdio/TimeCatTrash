package com.timecat.data.room.book

import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.PAGE_MD
import com.timecat.identity.data.block.type.PAGE_ORG
import com.timecat.identity.data.block.type.PAGE_Record

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-23
 * @description null
 * @usage null
 */

enum class RoomPage(val id: Int) {
    Record(PAGE_Record),
    MD(PAGE_MD),
    Org(PAGE_ORG)
}

var RoomRecord.orgBookId: Long
    get() = extension.getLong("orgBookId") ?: 0
    set(value) {
        extension.put("orgBookId", value)
    }

var RoomRecord.orgNoteId: Long
    get() = extension.getLong("orgNoteId") ?: 0
    set(value) {
        extension.put("orgNoteId", value)
    }
