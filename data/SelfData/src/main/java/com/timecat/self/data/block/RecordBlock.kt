package com.timecat.self.data.block

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.base.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description null
 * @usage null
 */
/**
 * RecordSection 兼容了任务、笔记、计划、子计划、笔记本、提醒、收藏集、清单
 * RecordSection 只是必要的控制结构，不会很大
 * 具体的比较长的文本信息存在 NoteBody，其 url 链接到本地文件里
 * 用户可用其他 app 修改文件，RecordSection 可读取其修改，实现"控制"和"编辑"的解耦
 * 具体的数据信息，如打卡，点赞等等，只能由 Action 统计得来，RecordSection 不知道
 *
 * 虽然它们共用一个 Section，但是类别还是要分的，这决定了它们在本地的渲染方式
 */
data class RecordBlock(
    val taskHeader: TaskHeader,
    val noteBody: NoteBody,
    val attachmentTail: AttachmentTail
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): RecordBlock {
            val taskHeader = TaskHeader.fromJson(jsonObject.getString("taskHeader"))
            val noteBody = NoteBody.fromJson(jsonObject.getString("noteBody"))
            val attachmentTail = AttachmentTail.fromJson(jsonObject.getString("attachmentTail"))
            return RecordBlock(
                taskHeader,
                noteBody,
                attachmentTail
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["taskHeader"] = taskHeader
        jsonObject["noteBody"] = noteBody
        jsonObject["attachmentTail"] = attachmentTail
        return jsonObject
    }

}