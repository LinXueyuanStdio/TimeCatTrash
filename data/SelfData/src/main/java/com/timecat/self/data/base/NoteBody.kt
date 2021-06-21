package com.timecat.self.data.base

import androidx.annotation.IntDef
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.randomColor
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description null
 * @usage null
 */
data class NoteBody(
    var theme: Int = 0,
    var color: Int = randomColor(),
    @ShowType var miniShowType: Int = FOLD,
    @RenderType var render_type: Int = RENDER_TYPE_MARKDOWN//渲染类型
) : Serializable, IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): NoteBody {
            val theme = jsonObject.getInteger("theme")
            val render_type = jsonObject.getInteger("render_type")
            val miniShowType = jsonObject.getInteger("miniShowType")
            val color = jsonObject.getInteger("color")
            return NoteBody(
                theme,
                color,
                miniShowType,
                render_type
            )
        }

        @IntDef(
            FOLD,
            EXPANDED,
            COLLAPSE_TO_PARENT
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class ShowType

        const val FOLD = 0

        const val EXPANDED = 1
        const val COLLAPSE_TO_PARENT = 2
    }

    override fun toJsonObject(): JSONObject {
        val j = JSONObject()
        j["theme"] = theme
        j["color"] = color
        j["miniShowType"] = miniShowType
        j["render_type"] = render_type
        return j
    }

}

@IntDef(
    RENDER_TYPE_Record,
    RENDER_TYPE_MARKDOWN,
    RENDER_TYPE_ORG
)
@Retention(AnnotationRetention.SOURCE)
annotation class RenderType

const val RENDER_TYPE_Record: Int = 0 //不渲染，只显示纯文本，不能编辑
const val RENDER_TYPE_MARKDOWN: Int = 1 // Markdown渲染
const val RENDER_TYPE_ORG: Int = 2 // Org 渲染

enum class Render(@RenderType val id: Int) {
    TYPE_Record(RENDER_TYPE_Record),
    TYPE_MARKDOWN(RENDER_TYPE_MARKDOWN),
    TYPE_ORG(RENDER_TYPE_ORG)
}