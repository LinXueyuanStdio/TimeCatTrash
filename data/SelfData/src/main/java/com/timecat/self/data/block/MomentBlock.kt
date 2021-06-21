package com.timecat.self.data.block

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.base.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-15
 * @description 动态块
 * @usage null
 */
data class MomentBlock(
    val content: NoteBody = NoteBody(),
    /**
     * 媒体域
     */
    val mediaScope: AttachmentTail? = null,
    /**
     * 话题域
     */
    val topicScope: TopicScope? = null,
    /**
     * @ 域
     */
    val atScope: AtScope? = null,
    /**
     * 地域
     */
    val posScope: PosScope? = null,
    /**
     * 转发域
     */
    val relayScope: RelayScope? = null
) : IJson {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): MomentBlock {
            val content = jsonObject.getJSONObject("content")
            val mediaScope: JSONObject? = jsonObject.getJSONObject("mediaScope")
            val topicScope: JSONObject? = jsonObject.getJSONObject("topicScope")
            val atScope: JSONObject? = jsonObject.getJSONObject("atScope")
            val posScope: JSONObject? = jsonObject.getJSONObject("posScope")
            val relayScope: JSONObject? = jsonObject.getJSONObject("relayScope")
            return MomentBlock(
                NoteBody.fromJson(content),
                mediaScope?.let { AttachmentTail.fromJson(it) },
                topicScope?.let { TopicScope.fromJson(it) },
                atScope?.let { AtScope.fromJson(it) },
                posScope?.let { PosScope.fromJson(it) },
                relayScope?.let { RelayScope.fromJson(it) }
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["content"] = content.toJsonObject()
        mediaScope?.let { jsonObject["mediaScope"] = it.toJsonObject() }
        topicScope?.let { jsonObject["topicScope"] = it.toJsonObject() }
        atScope?.let { jsonObject["atScope"] = it.toJsonObject() }
        posScope?.let { jsonObject["posScope"] = it.toJsonObject() }
        relayScope?.let { jsonObject["relayScope"] = it.toJsonObject() }
        return jsonObject
    }
}
