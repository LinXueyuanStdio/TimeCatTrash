package com.timecat.self.data.block

import androidx.annotation.IntDef
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
//region 高级结构：评论
// 评论
data class CommentBlock(
    @CommentType val type: Int,
    val structure: String
) : IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): CommentBlock {
            val type = jsonObject.getInteger("type")
            val structure = jsonObject.getString("structure")
            return CommentBlock(
                type,
                structure
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["type"] = type
        jsonObject["structure"] = structure
        return jsonObject
    }

}

@IntDef(
    COMMENT_SIMPLE,
    COMMENT_LONG,
    COMMENT_TEXT,
    COMMENT_VIDEO
)
@Retention(AnnotationRetention.SOURCE)
annotation class CommentType

//评论的子类
const val BLOCK_COMMENT_SIMPLE: Int = 16 // 普通评论
const val BLOCK_COMMENT_LONG: Int = 17 // 长评+评分
const val BLOCK_COMMENT_TEXT: Int = 18 // 划线、本章说
const val BLOCK_COMMENT_VIDEO: Int = 19 // 弹幕
const val COMMENT_SIMPLE: Int = BLOCK_COMMENT_SIMPLE // 普通评论
const val COMMENT_LONG: Int = BLOCK_COMMENT_LONG // 长评+评分
const val COMMENT_TEXT: Int = BLOCK_COMMENT_TEXT // 划线、本章说
const val COMMENT_VIDEO: Int = BLOCK_COMMENT_VIDEO // 弹幕

// 普通评论
data class SimpleComment(
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
        fun fromJson(jsonObject: JSONObject): SimpleComment {
            val content = jsonObject.getJSONObject("content")
            val mediaScope: JSONObject? = jsonObject.getJSONObject("mediaScope")
            val topicScope: JSONObject? = jsonObject.getJSONObject("topicScope")
            val atScope: JSONObject? = jsonObject.getJSONObject("atScope")
            val posScope: JSONObject? = jsonObject.getJSONObject("posScope")
            val relayScope: JSONObject? = jsonObject.getJSONObject("relayScope")
            return SimpleComment(
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

// 长评+评分
data class LongComment(
    val score: Int,
    val content: BlockMini
) : IJson {
    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["score"] = score
        jsonObject["content"] = content
        return jsonObject
    }

    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): LongComment {
            val score = jsonObject.getInteger("score")
            val content = jsonObject.getJSONObject("content")
            return LongComment(
                score,
                BlockMini.fromJson(content)
            )
        }
    }
}

// 划线、本章说
data class TextComment(
    val from: Int,
    val to: Int,
    val content: String
) : IJson {
    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["from"] = from
        jsonObject["to"] = to
        jsonObject["content"] = content
        return jsonObject
    }

    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): TextComment {
            val from = jsonObject.getInteger("from")
            val to = jsonObject.getInteger("to")
            val content = jsonObject.getString("content")
            return TextComment(
                from,
                to,
                content
            )
        }
    }
}

// 弹幕
data class VideoComment(
    val theme: Int,
    val begin: Long,
    val content: String
) : IJson {
    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["theme"] = theme
        jsonObject["begin"] = begin
        jsonObject["content"] = content
        return jsonObject
    }

    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): VideoComment {
            val theme = jsonObject.getInteger("theme")
            val begin = jsonObject.getLong("begin")
            val content = jsonObject.getString("content")
            return VideoComment(
                theme,
                begin,
                content
            )
        }
    }
}
//endregion