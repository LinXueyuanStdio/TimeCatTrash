package com.timecat.self.data

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.block.UpdateInfo

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
fun JSONObject.getUpdateInfoList(key: String): MutableList<UpdateInfo> {
    return getJSONArray(key)?.toUpdateInfoList() ?: mutableListOf()
}

fun JSONObject.getStringList(key: String): MutableList<String> {
    return getJSONArray(key)?.toListString() ?: mutableListOf()
}

fun JSONArray.toUpdateInfoList(): MutableList<UpdateInfo> {
    val list: MutableList<UpdateInfo> = mutableListOf()
    for (i in this) {
        list.add(UpdateInfo.fromJson(i as JSONObject))
    }
    return list
}

fun JSONArray.toListString(): MutableList<String> {
    val list: MutableList<String> = mutableListOf()
    for (i in this) {
        list.add((i as JSONObject).toString())
    }
    return list
}