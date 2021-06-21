package com.timecat.data.room.record

import androidx.room.TypeConverter
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.identity.data.base.IJson
import com.timecat.identity.data.base.Json
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-30
 * @description null
 * @usage null
 */
object JsonTypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromObjectToString(type: Json): String {
        return type.ext.toJSONString()
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToJSON(json: String): Json {
        return Json(JSONObject.parseObject(json))
    }

    @TypeConverter
    @JvmStatic
    fun fromJSONObjectToString(type: JSONObject): String {
        return type.toJSONString()
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToJSONObject(json: String): JSONObject {
        return JSONObject.parseObject(json)
    }
}