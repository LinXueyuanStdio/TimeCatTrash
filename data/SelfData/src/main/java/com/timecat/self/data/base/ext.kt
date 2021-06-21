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
data class Json(var ext: JSONObject = JSONObject()) : Serializable, IJson {

    companion object {
        @JvmStatic
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))

        @JvmStatic
        fun fromJson(jsonObject: JSONObject): Json {
            return Json(jsonObject)
        }
    }

    override fun toJsonObject(): JSONObject {
        return ext
    }

    override fun toString(): String {
        return toJson()
    }
}