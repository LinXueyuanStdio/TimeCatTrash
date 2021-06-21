package com.timecat.component.bmob.ext.bmob

import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.*
import com.timecat.component.bmob.ext.toDataError
import com.timecat.identity.data.service.DataError

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
class Saver<T : BmobObject> : RequestCallback<T>() {
    lateinit var target: T
    fun build() {
        target.save(object : SaveListener<String>() {
            override fun done(result: String?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    else -> {
                        onSuccess?.invoke(target)
                    }
                }
            }
        })
    }
}

fun saveBlock(create: Saver<Block>.() -> Unit) = save(create)
fun saveBlockRelation(create: Saver<Block2Block>.() -> Unit) = save(create)
fun saveUser(create: Saver<_User>.() -> Unit) = save(create)
fun saveUserRelation(create: Saver<User2User>.() -> Unit) = save(create)
fun saveAction(create: Saver<Action>.() -> Unit) = save(create)
fun saveInterAction(create: Saver<InterAction>.() -> Unit) = save(create)
fun <T : BmobObject> save(create: Saver<T>.() -> Unit) = Saver<T>().apply(create).also { it.build() }


class Deleter<T : BmobObject> : SimpleRequestCallback<T>() {
    lateinit var target: T
    lateinit var tableName: String
    fun build() {
        target.delete(object : UpdateListener() {
            override fun done(e: BmobException?) {
                if (e != null) {
                    onError?.invoke(e.toDataError())
                } else {
                    onSuccess?.invoke(target)
                }
            }
        })
    }
}

fun deleteBlock(create: Deleter<Block>.() -> Unit) = delete("Block", create)
fun deleteBlockRelation(create: Deleter<Block2Block>.() -> Unit) = delete("Block2Block", create)
fun deleteUser(create: Deleter<_User>.() -> Unit) = delete("_User", create)
fun deleteUserRelation(create: Deleter<User2User>.() -> Unit) = delete("User2User", create)
fun deleteAction(create: Deleter<Action>.() -> Unit) = delete("Action", create)
fun deleteInterAction(create: Deleter<InterAction>.() -> Unit) = delete("InterAction", create)
fun <T : BmobObject> delete(tableName: String, create: Deleter<T>.() -> Unit) = Deleter<T>().apply {
    this.tableName = tableName
    create()
}.also { it.build() }


class Updater<T : BmobObject> : SimpleRequestCallback<T>() {
    lateinit var target: T
    lateinit var tableName: String
    fun build() {
        target.tableName = tableName
        target.update(object : UpdateListener() {
            override fun done(e: BmobException?) {
                if (e != null) {
                    onError?.invoke(e.toDataError())
                } else {
                    onSuccess?.invoke(target)
                }
            }
        })
    }
}

fun updateBlock(create: Updater<Block>.() -> Unit) = update("Block", create)
fun updateUser(create: Updater<_User>.() -> Unit) = update("_User", create)
fun <T : BmobObject> update(tableName: String, create: Updater<T>.() -> Unit) = Updater<T>().apply {
    this.tableName = tableName
    create()
}.also { it.build() }


fun saveBatch(create: BatchSaver.() -> Unit) = BatchSaver().apply(create).also { it.build() }
class BatchSaver : RequestCallback<BatchResult>() {
    lateinit var target: List<BmobObject>
    fun build() {
        BmobBatch().insertBatch(target).doBatch(object : QueryListListener<BatchResult>() {
            override fun done(result: List<BatchResult>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    else -> {
                        for (i in result) {
                            if (!i.isSuccess) {
                                onError?.invoke(DataError(-1, i.error?.localizedMessage))
                                return
                            }
                        }
                        onSuccess?.invoke(result[0])
                    }
                }
            }
        })
    }
}

fun deleteBatch(create: BatchDeleter.() -> Unit) = BatchDeleter().apply(create).also { it.build() }
class BatchDeleter : RequestCallback<BatchResult>() {
    lateinit var target: List<BmobObject>
    fun build() {
        BmobBatch().deleteBatch(target).doBatch(object : QueryListListener<BatchResult>() {
            override fun done(result: List<BatchResult>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    else -> {
                        for (i in result) {
                            if (!i.isSuccess) {
                                onError?.invoke(DataError(-1, i.error?.localizedMessage))
                                return
                            }
                        }
                        onSuccess?.invoke(result[0])
                    }
                }
            }
        })
    }
}

fun deleteThenInsertBatch(create: BatchDeleteThenInsertor.() -> Unit) =
    BatchDeleteThenInsertor().apply(create).also { it.build() }

class BatchDeleteThenInsertor : RequestCallback<List<BatchResult>>() {
    lateinit var delete: List<BmobObject>
    lateinit var insert: List<BmobObject>
    fun build() {
        BmobBatch()
            .deleteBatch(delete)
            .insertBatch(insert)
            .doBatch(object : QueryListListener<BatchResult>() {
                override fun done(result: List<BatchResult>?, e: BmobException?) {
                    when {
                        e != null -> {
                            onError?.invoke(e.toDataError())
                        }
                        result == null -> {
                            onError?.invoke(DataError(-1, "空数据"))
                        }
                        else -> {
                            for (i in result) {
                                if (!i.isSuccess) {
                                    onError?.invoke(DataError(i.error?.errorCode ?: -1, i.error?.localizedMessage))
                                    return
                                }
                            }
                            onSuccess?.invoke(result)
                        }
                    }
                }
            })
    }
}
