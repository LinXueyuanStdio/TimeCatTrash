package com.timecat.self.data.base

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */

/**
 * 目标域，点击后跳转到详情页
 */
class TargetScope(
    /**
     * 决定查哪张表
     */
    val type: Int,
    val objectId: String
) : IJson {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): TargetScope {
            val type = jsonObject.getInteger("type")
            val objectId = jsonObject.getString("objectId")
            return TargetScope(
                type,
                objectId
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["type"] = type
        jsonObject["objectId"] = objectId
        return jsonObject
    }
}

/**
 * 标签域
 */
class TagScope(
    var tags: MutableList<TagItem>
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): TagScope {
            val jsonArray = jsonObject.getJSONArray("tags")
            val tags: MutableList<TagItem> = ArrayList()
            for (i in jsonArray) {
                tags.add(TagItem.fromJson(i as JSONObject))
            }
            return TagScope(tags)
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()
        jsonArray.addAll(tags)
        jsonObject["tags"] = jsonArray
        return jsonObject
    }
}

class TagItem(
    val name: String,
    val objectId: String
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): TagItem {
            val name = jsonObject.getString("name")
            val objectId = jsonObject.getString("objectId")
            return TagItem(
                name,
                objectId
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["name"] = name
        jsonObject["objectId"] = objectId
        return jsonObject
    }
}


/**
 * 地点域
 */
class PosScope(
    val name: String,
    val objectId: String // (x,y)  "$x,$y"
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): PosScope {
            val name = jsonObject.getString("name")
            val objectId = jsonObject.getString("objectId")
            return PosScope(
                name,
                objectId
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["name"] = name
        jsonObject["objectId"] = objectId
        return jsonObject
    }
}

class AtItem(
    val name: String,
    val objectId: String
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): AtItem {
            val name = jsonObject.getString("name")
            val objectId = jsonObject.getString("objectId")
            return AtItem(
                name,
                objectId
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["name"] = name
        jsonObject["objectId"] = objectId
        return jsonObject
    }
}

class TopicItem(
    val name: String,
    val objectId: String
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): TopicItem {
            val name = jsonObject.getString("name")
            val objectId = jsonObject.getString("objectId")
            return TopicItem(
                name,
                objectId
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["name"] = name
        jsonObject["objectId"] = objectId
        return jsonObject
    }
}

/**
 * 话题域
 */
class TopicScope(
    val topics: MutableList<TopicItem>
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): TopicScope {
            val jsonArray = jsonObject.getJSONArray("topics")
            val tags: MutableList<TopicItem> = mutableListOf()
            for (i in jsonArray) {
                tags.add(TopicItem.fromJson(i as JSONObject))
            }
            return TopicScope(tags)
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()
        jsonArray.addAll(topics)
        jsonObject["topics"] = jsonArray
        return jsonObject
    }
}

/**
 * @ 域
 */
class AtScope(
    val ats: MutableList<AtItem>
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): AtScope {
            val jsonArray = jsonObject.getJSONArray("ats")
            val ats: MutableList<AtItem> = mutableListOf()
            for (i in jsonArray) {
                ats.add(AtItem.fromJson(i as JSONObject))
            }
            return AtScope(ats)
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()
        jsonArray.addAll(ats)
        jsonObject["ats"] = jsonArray
        return jsonObject
    }
}


/**
 * 隐私域
 */
class PrivacyScope(
    var isPrivate: Boolean = false,
    var isPublic: Boolean = true,
    val whoCanSee: String? = null,
    val whoCannotSee: String? = null
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): PrivacyScope {
            val isPrivate = jsonObject.getBoolean("isPrivate")
            val isPublic = jsonObject.getBoolean("isPublic")
            val whoCanSee: String? = jsonObject.getString("whoCanSee")
            val whoCannotSee: String? = jsonObject.getString("whoCannotSee")
            return PrivacyScope(
                isPrivate, isPublic, whoCanSee, whoCannotSee
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["isPrivate"] = isPrivate
        jsonObject["isPublic"] = isPublic
        jsonObject["whoCanSee"] = whoCanSee
        jsonObject["whoCannotSee"] = whoCannotSee
        return jsonObject
    }
}

/**
 * 转发域
 */
class RelayScope(
    val objectId: String
) : IJson {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): RelayScope {
            val objectId = jsonObject.getString("objectId")
            return RelayScope(
                objectId
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["objectId"] = objectId
        return jsonObject
    }
}