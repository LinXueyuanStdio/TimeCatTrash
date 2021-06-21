package com.timecat.self.data.base

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-15
 * @description null
 * @usage null
 */
data class PageHeader(
    /**
     * 最小，24dp
     */
    var icon: String = "R.drawable.ic_comment",
    /**
     * 头像
     */
    var avatar: String = "R.drawable.ic_comment",
    /**
     * 封面，大
     */
    var cover: String = "R.drawable.ic_comment"
) : Serializable, IJson {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): PageHeader {
            val icon = jsonObject.getString("icon")
            val avatar = jsonObject.getString("avatar")
            val cover = jsonObject.getString("cover")
            return PageHeader(icon, avatar, cover)
        }
    }

    override fun toJsonObject(): JSONObject {
        val j = JSONObject()
        j["icon"] = icon
        j["avatar"] = avatar
        j["cover"] = cover
        return j
    }

}
