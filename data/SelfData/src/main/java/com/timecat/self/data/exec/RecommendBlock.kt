package com.timecat.self.data.exec

import androidx.annotation.IntDef
import androidx.annotation.LongDef
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.base.IJson

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */

// 推荐
data class RecommendBlock(
    var title: String,
    var content: String,
    var extra: String = "",
    @RecommendType val type: Int,
    val structure: String
) : IJson {
    companion object {
        @LongDef(
            RECOMMEND_WAITING_TO_CHECK,
            RECOMMEND_ACCEPTED,
            RECOMMEND_REJECTED,
            RECOMMEND_CANCEL,
            RECOMMEND_RECHECK
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class RecommendStatus

        const val RECOMMEND_WAITING_TO_CHECK: Long = 0L//待审核
        const val RECOMMEND_ACCEPTED: Long = 1L//接受
        const val RECOMMEND_REJECTED: Long = 2L//拒绝
        const val RECOMMEND_CANCEL: Long = 3L//取消推荐
        const val RECOMMEND_RECHECK: Long = 4L//再次推荐

        @IntDef(
            RECOMMEND_LEADERBOARD,
            RECOMMEND_WebApp_To_LeaderBoard
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class RecommendType

        const val RECOMMEND_LEADERBOARD = 0
        const val RECOMMEND_WebApp_To_LeaderBoard = 1

        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): RecommendBlock {
            val title = jsonObject.getString("title")
            val content = jsonObject.getString("content")
            val extra = jsonObject.getString("extra")
            val type = jsonObject.getInteger("type")
            val structure = jsonObject.getString("structure")
            return RecommendBlock(
                title,
                content,
                extra,
                type,
                structure
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val j = JSONObject()
        j["title"] = title
        j["content"] = content
        j["extra"] = extra
        j["type"] = type
        j["structure"] = structure
        return j
    }

    fun getStatusStr(status: Long): String = when (status) {
        RECOMMEND_WAITING_TO_CHECK -> "待审核"
        RECOMMEND_ACCEPTED -> "接受"
        RECOMMEND_REJECTED -> "拒绝"
        RECOMMEND_CANCEL -> "取消推荐"
        RECOMMEND_RECHECK -> "再次推荐"
        else -> "不支持的状态，请升级"
    }
}

//推荐 parent 给 targetLeaderBoard
data class RecommendLeaderBoard(

    var blockId: String,
    var targetLeaderBoardId: String
) : IJson {
    override fun toJsonObject(): JSONObject {
        val j = JSONObject()

        j["blockId"] = blockId
        j["targetLeaderBoardId"] = targetLeaderBoardId
        return j
    }

    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): RecommendLeaderBoard {

            val blockId = jsonObject.getString("blockId")
            val targetLeaderBoardId = jsonObject.getString("targetLeaderBoardId")
            return RecommendLeaderBoard(
                blockId, targetLeaderBoardId
            )
        }
    }
}