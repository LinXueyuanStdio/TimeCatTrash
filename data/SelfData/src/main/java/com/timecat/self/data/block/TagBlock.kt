package com.timecat.self.data.block

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.base.AttachmentTail
import com.timecat.self.data.base.IJson
import com.timecat.self.data.base.NoteBody
import com.timecat.self.data.base.PageHeader

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/8
 * @description 标签控制块
 * @usage null
 */
data class TagBlock(
    val content: NoteBody = NoteBody(),
    val header: PageHeader? = null,
    /**
     * 媒体域
     */
    val mediaScope: AttachmentTail? = null,
): IJson {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): TagBlock {
            val content = jsonObject.getJSONObject("content") ?: NoteBody().toJsonObject()
            val mediaScope: JSONObject? = jsonObject.getJSONObject("mediaScope")
            val header: JSONObject? = jsonObject.getJSONObject("header")
            return TagBlock(
                NoteBody.fromJson(content),
                header?.let { PageHeader.fromJson(it) },
                mediaScope?.let { AttachmentTail.fromJson(it) },
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["content"] = content.toJsonObject()
        mediaScope?.let { jsonObject["mediaScope"] = it.toJsonObject() }
        header?.let { jsonObject["header"] = it.toJsonObject() }
        return jsonObject
    }
}
