package com.timecat.component.bmob.ext

import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.ext.net.allFans
import com.timecat.component.bmob.ext.bmob.requestActionCount

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
fun Block.isUsed() = incrementAndUpdate("usedBy")

fun Block.isCommented(onDone: ((e: BmobException?) -> Unit)? = null) =
    incrementAndUpdate("comments", onDone)

fun Block.isLiked(onDone: ((e: BmobException?) -> Unit)? = null) =
    incrementAndUpdate("likes", onDone)

fun Block.isRelays(onDone: ((e: BmobException?) -> Unit)? = null) =
    incrementAndUpdate("relays", onDone)

fun Block.isFollowed(onDone: ((e: BmobException?) -> Unit)? = null) =
    incrementAndUpdate("followers", onDone)

fun Block.isUnCommented(onDone: ((e: BmobException?) -> Unit)? = null) =
    decrementAndUpdate("comments", onDone)

fun Block.isUnLiked(onDone: ((e: BmobException?) -> Unit)? = null) =
    decrementAndUpdate("likes", onDone)

fun Block.isUnRelays(onDone: ((e: BmobException?) -> Unit)? = null) =
    decrementAndUpdate("relays", onDone)

fun Block.isUnFollowed(onDone: ((e: BmobException?) -> Unit)? = null) =
    decrementAndUpdate("followers", onDone)

fun Block.incrementAndUpdate(key: String, onDone: ((e: BmobException?) -> Unit)? = null) {
    if (!objectId.isNullOrBlank()) {
        tableName = "Block"
        increment(key)
        update(objectId, object : UpdateListener() {
            override fun done(e: BmobException?) {
                e?.printStackTrace()
                onDone?.invoke(e)
            }
        })
    }
}

fun Block.decrementAndUpdate(key: String, onDone: ((e: BmobException?) -> Unit)? = null) {
    if (!objectId.isNullOrBlank()) {
        tableName = "Block"
        increment(key, -1)
        update(objectId, object : UpdateListener() {
            override fun done(e: BmobException?) {
                e?.printStackTrace()
                onDone?.invoke(e)
            }
        })
    }
}

fun Block.focusSum(onDaoCount: (Int) -> Unit) {
    requestActionCount {
        query = allFans()
        onSuccess = onDaoCount
    }
}

