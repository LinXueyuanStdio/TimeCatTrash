package com.timecat.self.data.block

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.base.IJson

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description 使用 child 字段实现 list
 * @usage null
 */
data class ListBlock(
    val theme: Int = 0
) : IJson {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): ListBlock {
            val theme = jsonObject.getInteger("theme")
            return ListBlock(
                theme
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("theme", theme)
        return jsonObject
    }
}