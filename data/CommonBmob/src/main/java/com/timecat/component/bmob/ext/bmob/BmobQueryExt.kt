package com.timecat.component.bmob.ext.bmob

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.CountListener
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.*
import com.timecat.component.bmob.ext.toDataError
import com.timecat.identity.data.one.User
import com.timecat.identity.data.service.DataError
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
fun <T> request(create: Request<T>.() -> Unit) = Request<T>().apply(create).also { it.build() }
class Request<T> : RequestCallback<T>() {
    lateinit var query: BmobQuery<T>
    fun build() {
        query.findObjects(object : FindListener<T>() {
            override fun done(result: MutableList<T>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    result.isEmpty() -> {
                        onEmpty?.invoke()
                    }
                    result.size == 1 -> {
                        onSuccess?.invoke(result[0])
                    }
                    else -> {
                        onListSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}

fun requestBlock(create: RequestBlock.() -> Unit) = RequestBlock().apply(create).also { it.build() }
class RequestBlock : RequestCallback<Block>() {
    lateinit var query: BmobQuery<Block>
    fun build() {
        query.findObjects(object : FindListener<Block>() {
            override fun done(result: MutableList<Block>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    result.isEmpty() -> {
                        onEmpty?.invoke()
                    }
                    result.size == 1 -> {
                        onSuccess?.invoke(result[0])
                    }
                    else -> {
                        onListSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}

fun requestExist(create: RequestExist.() -> Unit) = RequestExist().apply(create).also { it.build() }
class RequestExist : SimpleRequestCallback<Boolean>() {
    lateinit var query: BmobQuery<Block>
    fun build() {
        query.count(Block::class.java, object : CountListener() {
            override fun done(result: Int?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onSuccess?.invoke(false)
                    }
                    result <= 0 -> {
                        onSuccess?.invoke(false)
                    }
                    else -> {
                        onSuccess?.invoke(true)
                    }
                }
            }
        })
    }
}
fun requestInterActionExist(create: RequestInterActionExist.() -> Unit) =
    RequestInterActionExist().apply(create).also { it.build() }
class RequestInterActionExist : SimpleRequestCallback<Boolean>() {
    lateinit var query: BmobQuery<InterAction>
    fun build() {
        query.count(InterAction::class.java, object : CountListener() {
            override fun done(result: Int?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onSuccess?.invoke(false)
                    }
                    result <= 0 -> {
                        onSuccess?.invoke(false)
                    }
                    else -> {
                        onSuccess?.invoke(true)
                    }
                }
            }
        })
    }
}

fun requestActionCount(create: RequestActionCount.() -> Unit) =
    RequestActionCount().apply(create).also { it.build() }

class RequestActionCount : SimpleRequestCallback<Int>() {
    lateinit var query: BmobQuery<Action>
    fun build() {
        query.count(Action::class.java, object : CountListener() {
            override fun done(result: Int?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onSuccess?.invoke(0)
                    }
                    result <= 0 -> {
                        onSuccess?.invoke(0)
                    }
                    else -> {
                        onSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}
fun requestUserRelationCount(create: RequestUserRelationCount.() -> Unit) =
    RequestUserRelationCount().apply(create).also { it.build() }

class RequestUserRelationCount : SimpleRequestCallback<Int>() {
    lateinit var query: BmobQuery<User2User>
    fun build() {
        query.count(User2User::class.java, object : CountListener() {
            override fun done(result: Int?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onSuccess?.invoke(0)
                    }
                    result <= 0 -> {
                        onSuccess?.invoke(0)
                    }
                    else -> {
                        onSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}


fun requestUser(create: RequestUser.() -> Unit) = RequestUser().apply(create).also { it.build() }
class RequestUser : RequestCallback<_User>() {
    lateinit var query: BmobQuery<_User>
    fun build() {
        query.findObjects(object : FindListener<_User>() {
            override fun done(result: MutableList<_User>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    result.isEmpty() -> {
                        onEmpty?.invoke()
                    }
                    result.size == 1 -> {
                        onSuccess?.invoke(result[0])
                    }
                    else -> {
                        onListSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}

fun requestAction(create: RequestAction.() -> Unit) = RequestAction().apply(create).also { it.build() }
class RequestAction : RequestCallback<Action>() {
    lateinit var query: BmobQuery<Action>
    fun build() {
        query.findObjects(object : FindListener<Action>() {
            override fun done(result: MutableList<Action>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    result.isEmpty() -> {
                        onEmpty?.invoke()
                    }
                    result.size == 1 -> {
                        onSuccess?.invoke(result[0])
                    }
                    else -> {
                        onListSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}

fun requestUserRelation(create: RequestUserRelation.() -> Unit) =
    RequestUserRelation().apply(create).also { it.build() }

class RequestUserRelation : RequestCallback<User2User>() {
    lateinit var query: BmobQuery<User2User>
    fun build() {
        query.findObjects(object : FindListener<User2User>() {
            override fun done(result: MutableList<User2User>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    result.isEmpty() -> {
                        onEmpty?.invoke()
                    }
                    result.size == 1 -> {
                        onSuccess?.invoke(result[0])
                    }
                    else -> {
                        onListSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}

fun requestBlockRelation(create: RequestBlockRelation.() -> Unit) =
    RequestBlockRelation().apply(create).also { it.build() }

class RequestBlockRelation : RequestCallback<Block2Block>() {
    lateinit var query: BmobQuery<Block2Block>
    fun build() {
        query.findObjects(object : FindListener<Block2Block>() {
            override fun done(result: MutableList<Block2Block>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    result.isEmpty() -> {
                        onEmpty?.invoke()
                    }
                    result.size == 1 -> {
                        onSuccess?.invoke(result[0])
                    }
                    else -> {
                        onListSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}

fun requestInterAction(create: RequestInterAction.() -> Unit) =
    RequestInterAction().apply(create).also { it.build() }

class RequestInterAction : RequestCallback<InterAction>() {
    lateinit var query: BmobQuery<InterAction>
    fun build() {
        query.findObjects(object : FindListener<InterAction>() {
            override fun done(result: MutableList<InterAction>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    result.isEmpty() -> {
                        onEmpty?.invoke()
                    }
                    result.size == 1 -> {
                        onSuccess?.invoke(result[0])
                    }
                    else -> {
                        onListSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}

fun requestStatistics(create: RequestStatistics.() -> Unit) =
    RequestStatistics().apply(create).also { it.build() }

class RequestStatistics : RequestCallback<Block2Block>() {
    lateinit var query: BmobQuery<Block2Block>
    fun build() {
        query.findObjects(object : FindListener<Block2Block>() {
            override fun done(result: MutableList<Block2Block>?, e: BmobException?) {
                when {
                    e != null -> {
                        onError?.invoke(e.toDataError())
                    }
                    result == null -> {
                        onError?.invoke(DataError(-1, "空数据"))
                    }
                    result.isEmpty() -> {
                        onEmpty?.invoke()
                    }
                    result.size == 1 -> {
                        onSuccess?.invoke(result[0])
                    }
                    else -> {
                        onListSuccess?.invoke(result)
                    }
                }
            }
        })
    }
}

/**
 * TODO 不带groupby的查询结果，统计全部。
 * [{
 *  "_avgFault": 1.625,
 *  "_avgFoul": 3.75,
 *  "_avgScore": 25.75,
 *  "_avgSteal": 2,
 *  "_count": 79,
 *  "_maxFault": 3,
 *  "_maxFoul": 6,
 *  "_maxScore": 53,
 *  "_maxSteal": 4,
 *  "_minFault": 1,
 *  "_minFoul": 2,
 *  "_minScore": 11,
 *  "_minSteal": 1,
 *  "_sumFault": 13,
 *  "_sumFoul": 30,
 *  "_sumScore": 206,
 *  "_sumSteal": 16
 * }]
 */
/**
 * TODO 带groupby的查询结果，根据country分组统计。
 * [{
 * "_avgFault": 1.6666666666666667,
 * "_avgFoul": 2.3333333333333335,
 * "_avgScore": 25.666666666666668,
 * "_avgSteal": 1.3333333333333333,
 * "_count": 3,
 * "_maxFault": 2,
 * "_maxFoul": 3,
 * "_maxScore": 53,
 * "_maxSteal": 2,
 * "_minFault": 1,
 * "_minFoul": 2,
 * "_minScore": 12,
 * "_minSteal": 1,
 * "_sumFault": 5,
 * "_sumFoul": 7,
 * "_sumScore": 77,
 * "_sumSteal": 4,
 * "country": "china"
 * }, {
 * "_avgFault": 2,
 * "_avgFoul": 4.5,
 * "_avgScore": 22,
 * "_avgSteal": 2.5,
 * "_count": 2,
 * "_maxFault": 3,
 * "_maxFoul": 5,
 * "_maxScore": 23,
 * "_maxSteal": 3,
 * "_minFault": 1,
 * "_minFoul": 4,
 * "_minScore": 21,
 * "_minSteal": 2,
 * "_sumFault": 4,
 * "_sumFoul": 9,
 * "_sumScore": 44,
 * "_sumSteal": 5,
 * "country": "usa"
 * }, {
 * "_avgFault": 1.3333333333333333,
 * "_avgFoul": 4.666666666666667,
 * "_avgScore": 28.333333333333332,
 * "_avgSteal": 2.3333333333333335,
 * "_count": 3,
 * "_maxFault": 2,
 * "_maxFoul": 6,
 * "_maxScore": 43,
 * "_maxSteal": 4,
 * "_minFault": 1,
 * "_minFoul": 2,
 * "_minScore": 11,
 * "_minSteal": 1,
 * "_sumFault": 4,
 * "_sumFoul": 14,
 * "_sumScore": 85,
 * "_sumSteal": 7,
 * "country": "uk"
 * }, {
 * "_avgFault": null,
 * "_avgFoul": null,
 * "_avgScore": null,
 * "_avgSteal": null,
 * "_count": 71,
 * "_maxFault": null,
 * "_maxFoul": null,
 * "_maxScore": null,
 * "_maxSteal": null,
 * "_minFault": null,
 * "_minFoul": null,
 * "_minScore": null,
 * "_minSteal": null,
 * "_sumFault": 0,
 * "_sumFoul": 0,
 * "_sumScore": 0,
 * "_sumSteal": 0,
 * "country": null
 * }]
 */
/**
 * TODO 带groupby和having的查询结果
 * [{
 *  "_avgFault": 1.3333333333333333,
 *  "_avgFoul": 4.666666666666667,
 *  "_avgScore": 28.333333333333332,
 *  "_avgSteal": 2.3333333333333335,
 *  "_count": 3,
 *  "_maxFault": 2,
 *  "_maxFoul": 6,
 *  "_maxScore": 43,
 *  "_maxSteal": 4,
 *  "_minFault": 1,
 *  "_minFoul": 2,
 *  "_minScore": 11,
 *  "_minSteal": 1,
 *  "_sumFault": 4,
 *  "_sumFoul": 14,
 *  "_sumScore": 85,
 *  "_sumSteal": 7,
 *  "country": "uk"
 * }]
 */
/**
 * “group by”从字面意义上理解就是根据“by”指定的规则对数据进行分组，所谓的分组就是将一个“数据集”划分成若干个“小区域”，然后针对若干个“小区域”进行数据处理。
 * where 子句的作用是在对查询结果进行分组前，将不符合where条件的行去掉，即在分组之前过滤数据，where条件中不能包含聚组函数，使用where条件过滤出特定的行。
 * having 子句的作用是筛选满足条件的组，即在分组之后过滤数据，条件中经常包含聚组函数，使用having 条件过滤出特定的组，也可以使用多个分组标准进行分组。
 *
 * @throws JSONException
 */
@Throws(JSONException::class)
private fun statistics() {
    val bmobQuery: BmobQuery<User> = BmobQuery<User>()
    //总和
    bmobQuery.sum(arrayOf("score", "steal", "foul", "fault"))
    //平均值
    bmobQuery.average(arrayOf("score", "steal", "foul", "fault"))
    //最大值
    bmobQuery.max(arrayOf("score", "steal", "foul", "fault"))
    //最小值
    bmobQuery.min(arrayOf("score", "steal", "foul", "fault"))
    //是否返回所统计的总条数
    bmobQuery.setHasGroupCount(true)
    //根据所给列分组统计
    bmobQuery.groupby(arrayOf("country"))
    //对统计结果进行过滤
    val map: HashMap<String, Any> = HashMap(1)
    val jsonObject = JSONObject()
    jsonObject.put("\$gt", 28)
    map["_avgScore"] = jsonObject
    bmobQuery.having(map)
    //开始统计查询
    bmobQuery.findStatistics(User::class.java, object : QueryListener<JSONArray?>() {
        override fun done(jsonArray: JSONArray?, e: BmobException?) {
            if (e == null) {
                try {
                    val jsonObject: JSONObject? = jsonArray?.getJSONObject(0)
                    val sum: Int? = jsonObject?.getInt("_sumScore")
                } catch (e1: JSONException) {
                    e1.printStackTrace()
                }
            } else {
            }
        }
    })
}