package com.timecat.self.data.block

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.base.IJson
import com.timecat.self.data.getStringList
import com.timecat.self.data.getUpdateInfoList

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/15
 * @description 插件
 * @usage null
 */
data class PluginApp(
    var packageName: String = "",
    /**
     * 开发者
     */
    var developer: String = "",
    var partKey: String = "",
    /**
     * 开源地址
     */
    var openSourceUrl: String = "",
    /**
     * 历次更新的版本信息
     * 每次更新都要修改一次，需要保持历史
     */
    var updateInfo: MutableList<UpdateInfo> = mutableListOf(),
    /**
     * 预览该 App 的媒体资源
     */
    var preview_urls: MutableList<String> = mutableListOf(),
    /**
     * 提供的 Activity 列表
     * 每一项是 Activity 的完整名字
     */
    var activity_class_names: MutableList<String> = mutableListOf(),
    /**
     * 提供的 Service 列表
     * 每一项是 Service 的完整名字
     */
    var service_class_names: MutableList<String> = mutableListOf()
) : IJson {
    override fun toJsonObject(): JSONObject {
        val j = JSONObject()
        j["packageName"] = packageName
        j["developer"] = developer
        j["openSourceUrl"] = openSourceUrl
        j["partKey"] = partKey
        j["updateInfo"] = updateInfo
        j["preview_urls"] = preview_urls
        j["activity_class_names"] = activity_class_names
        j["service_class_names"] = service_class_names
        return j
    }

    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): PluginApp {
            val packageName = jsonObject.getString("packageName")
            val developer = jsonObject.getString("developer")
            val openSourceUrl = jsonObject.getString("openSourceUrl")
            val partKey = jsonObject.getString("partKey")
            val updateInfo = jsonObject.getUpdateInfoList("updateInfo")
            val preview_urls = jsonObject.getStringList("preview_urls")
            val activity_class_names = jsonObject.getStringList("activity_class_names")
            val service_class_names = jsonObject.getStringList("service_class_names")
            return PluginApp(
                packageName,
                developer,
                partKey,
                openSourceUrl,
                updateInfo,
                preview_urls,
                activity_class_names,
                service_class_names
            )
        }
    }
}
