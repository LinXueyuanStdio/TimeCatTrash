package com.timecat.self.data.base

import com.alibaba.fastjson.JSONObject

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description null
 * @usage null
 */
interface IJson {
    fun toJson(): String = toJsonObject().toJSONString()
    fun toJsonObject(): JSONObject
}