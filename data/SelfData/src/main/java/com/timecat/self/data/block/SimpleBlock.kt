package com.timecat.self.data.block

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description null
 * @usage null
 */
// region 缩略结构
data class BlockMini(var id: String) {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): BlockMini {
            return BlockMini(
                jsonObject.getString("id")
            )
        }

        fun fromJson(jsonArray: JSONArray): MutableList<BlockMini> {
            val list: MutableList<BlockMini> = mutableListOf()
            for (i in jsonArray) {
                list.add(fromJson(i as JSONObject))
            }
            return list
        }
    }
}
//endregion

// 1. 纯数据嵌入为json到数据库
// 2. 可被view直接修改到对象