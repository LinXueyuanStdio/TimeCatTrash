package com.timecat.data.room

import androidx.room.TypeConverter
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.timecat.identity.data.base.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-30
 * @description null
 * @usage null
 */
object TypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromLifeCycleToString(type: LifeCycle): String {
        return type.toJson()
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToLifeCycle(json: String): LifeCycle {
        return LifeCycle.fromJson(json)
    }


    @TypeConverter
    @JvmStatic
    fun fromListCycleToString(type: MutableList<Cycle>): String {
        return JSONArray.toJSONString(type)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToListCycle(json: String): MutableList<Cycle> {
        val jsonArray = JSONArray.parseArray(json)
        val list = mutableListOf<Cycle>()
        for (i in jsonArray) {
            list.add(Cycle.fromJson(i as JSONObject))
        }
        return list
    }

    @TypeConverter
    @JvmStatic
    fun fromCycleToString(type: Cycle): String {
        return type.toJson()
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToCycle(json: String): Cycle {
        return Cycle.fromJson(json)
    }

    @TypeConverter
    @JvmStatic
    fun fromListTimeToString(type: MutableList<Time>): String {
        return JSONArray.toJSONString(type)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToListTime(json: String): MutableList<Time> {
        val jsonArray = JSONArray.parseArray(json)
        val list = mutableListOf<Time>()
        for (i in jsonArray) {
            list.add(Time.fromJson(i as JSONObject))
        }
        return list
    }

    @TypeConverter
    @JvmStatic
    fun fromTimeToString(type: Time): String {
        return type.toJson()
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToTime(json: String): Time {
        return Time.fromJson(json)
    }

    @TypeConverter
    @JvmStatic
    fun fromAttachmentTailToString(type: AttachmentTail): String {
        return type.toJson()
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToAttachmentTail(json: String): AttachmentTail {
        return AttachmentTail.fromJson(json)
    }

    @TypeConverter
    @JvmStatic
    fun fromListAttachmentItemToString(type: MutableList<AttachmentItem>): String {
        return JSONArray.toJSONString(type)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToListAttachmentItem(json: String): MutableList<AttachmentItem> {
        val jsonArray = JSONArray.parseArray(json)
        val list = mutableListOf<AttachmentItem>()
        for (i in jsonArray) {
            list.add(AttachmentItem.fromJson(i as JSONObject))
        }
        return list
    }

    @TypeConverter
    @JvmStatic
    fun fromAttachmentItemToString(type: AttachmentItem): String {
        return type.toJson()
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToAttachmentItem(json: String): AttachmentItem {
        return AttachmentItem.fromJson(json)
    }

}