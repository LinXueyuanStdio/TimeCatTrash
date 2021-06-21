package com.timecat.self.data.base

import androidx.annotation.IntDef
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description null
 * @usage null
 */
data class AttachmentTail(
    val attachmentItems: MutableList<AttachmentItem>
) : Serializable, IAttachmentTail, IJson {
    override fun removeAllAttachment() = attachmentItems.clear()

    override fun size(): Int = attachmentItems.size

    override fun isEmpty(): Boolean = attachmentItems.isEmpty()

    override fun get(type: Int): MutableList<String> = attachmentItems
        .filter { it.type == type }
        .map { it.attachment }
        .toMutableList()

    fun getAllPath(): MutableList<String> = attachmentItems
        .map { it.attachment }
        .toMutableList()

    override fun isAll(type: Int): Boolean {
        return get(type).size == attachmentItems.size
    }


    override fun add(type: Int, a: String) {
        attachmentItems.add(AttachmentItem(type, a))
    }

    override fun remove(type: Int, a: String) {
        attachmentItems.find {
            (it.type == type) and (it.attachment == a)
        }?.let {
            attachmentItems.remove(it)
        }
    }

    override fun update(type: Int, a: String) {
        attachmentItems.find {
            (it.type == type) and (it.attachment == a)
        }?.attachment = a
    }
    //endregion

    //region 3. IJson<AttachmentTail>
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): AttachmentTail {
            val jsonArray = jsonObject.getJSONArray("attachmentItems")
            val attachmentItems: MutableList<AttachmentItem> = mutableListOf()
            for (i in jsonArray) {
                attachmentItems.add(AttachmentItem.fromJson(i as JSONObject))
            }
            return AttachmentTail(attachmentItems)
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()
        jsonArray.addAll(attachmentItems)
        jsonObject["attachmentItems"] = jsonArray
        return jsonObject
    }
    //endregion
}

data class AttachmentItem(
    @AttachmentItemType var type: Int,
    var attachment: String
) : Serializable, IJson {
    //region 3. IJson<AttachmentItem>
    companion object {
        // 包裹范围内 属于静态方法
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): AttachmentItem {
            return AttachmentItem(
                jsonObject.getInteger("type"),
                jsonObject.getString("attachment")
            )
        }

        @JvmStatic
        fun ofUnknown(path: String): AttachmentItem = of(AIT_UNKNOWN, path)

        @JvmStatic
        fun ofCode(path: String): AttachmentItem = of(AIT_CODE, path)

        @JvmStatic
        fun ofPhoto(path: String): AttachmentItem = of(AIT_PHOTO, path)

        @JvmStatic
        fun ofSound(path: String): AttachmentItem = of(AIT_SOUND, path)

        @JvmStatic
        fun ofVideo(path: String): AttachmentItem = of(AIT_VIDEO, path)

        @JvmStatic
        fun ofPPT(path: String): AttachmentItem = of(AIT_PPT, path)

        @JvmStatic
        fun ofPDF(path: String): AttachmentItem = of(AIT_PDF, path)

        @JvmStatic
        fun of(type: Int, path: String): AttachmentItem = AttachmentItem(type, path)
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        jsonObject.put("attachment", attachment)
        return jsonObject
    }

    fun isUnknown(): Boolean = isWhat(AIT_UNKNOWN)
    fun isCode(): Boolean = isWhat(AIT_CODE)
    fun isPhoto(): Boolean = isWhat(AIT_PHOTO)
    fun isSound(): Boolean = isWhat(AIT_SOUND)
    fun isVideo(): Boolean = isWhat(AIT_VIDEO)
    fun isPPT(): Boolean = isWhat(AIT_PPT)
    fun isPDF(): Boolean = isWhat(AIT_PDF)

    fun isWhat(@AttachmentItemType type: Int): Boolean = this.type == type
    //endregion
}

interface IAttachmentTail {
    fun addUnknown(a: String) = add(AIT_UNKNOWN, a)
    fun addCode(a: String) = add(AIT_CODE, a)
    fun addPhoto(a: String) = add(AIT_PHOTO, a)
    fun addSound(a: String) = add(AIT_SOUND, a)
    fun addVideo(a: String) = add(AIT_VIDEO, a)
    fun addPPT(a: String) = add(AIT_PPT, a)
    fun addPDF(a: String) = add(AIT_PDF, a)

    fun removeUnknown(a: String) = remove(AIT_UNKNOWN, a)
    fun removeCode(a: String) = remove(AIT_CODE, a)
    fun removePhoto(a: String) = remove(AIT_PHOTO, a)
    fun removeSound(a: String) = remove(AIT_SOUND, a)
    fun removeVideo(a: String) = remove(AIT_VIDEO, a)
    fun removePPT(a: String) = remove(AIT_PPT, a)
    fun removePDF(a: String) = remove(AIT_PDF, a)

    fun updateUnknow(a: String) = update(AIT_UNKNOWN, a)
    fun updateCode(a: String) = update(AIT_CODE, a)
    fun updatePhoto(a: String) = update(AIT_PHOTO, a)
    fun updateSound(a: String) = update(AIT_SOUND, a)
    fun updateVideo(a: String) = update(AIT_VIDEO, a)
    fun updatePPT(a: String) = update(AIT_PPT, a)
    fun updatePDF(a: String) = update(AIT_PDF, a)

    fun getUnknown(): MutableList<String> = get(AIT_UNKNOWN)
    fun getCode(): MutableList<String> = get(AIT_CODE)
    fun getPhoto(): MutableList<String> = get(AIT_PHOTO)
    fun getSound(): MutableList<String> = get(AIT_SOUND)
    fun getVideo(): MutableList<String> = get(AIT_VIDEO)
    fun getPPT(): MutableList<String> = get(AIT_PPT)
    fun getPDF(): MutableList<String> = get(AIT_PDF)

    fun isAllUnknown(): Boolean = isAll(AIT_UNKNOWN)
    fun isAllCode(): Boolean = isAll(AIT_CODE)
    fun isAllPhoto(): Boolean = isAll(AIT_PHOTO)
    fun isAllSound(): Boolean = isAll(AIT_SOUND)
    fun isAllVideo(): Boolean = isAll(AIT_VIDEO)
    fun isAllPPT(): Boolean = isAll(AIT_PPT)
    fun isAllPDF(): Boolean = isAll(AIT_PDF)

    fun isAll(@AttachmentItemType type: Int): Boolean
    fun add(@AttachmentItemType type: Int, a: String)
    fun remove(@AttachmentItemType type: Int, a: String)
    fun update(@AttachmentItemType type: Int, a: String)
    fun get(@AttachmentItemType type: Int): MutableList<String>
    fun removeAllAttachment()
    fun size(): Int
    fun isEmpty(): Boolean
}

@IntDef(
    AIT_UNKNOWN,
    AIT_CODE,
    AIT_PHOTO,
    AIT_SOUND,
    AIT_VIDEO,
    AIT_PPT,
    AIT_PDF
)
@Retention(AnnotationRetention.SOURCE)
annotation class AttachmentItemType

const val AIT_UNKNOWN = 0
const val AIT_CODE = 1
const val AIT_PHOTO = 2
const val AIT_SOUND = 3
const val AIT_VIDEO = 4
const val AIT_PPT = 5
const val AIT_PDF = 6

val codeTypeTail = arrayOf(".py", ".c", ".cpp")
val photoTypeTail = arrayOf(".jpg", ".png", ".jpeg", ".webp")
val soundTypeTail = arrayOf(".mp3", ".wav")
val videoTypeTail = arrayOf(".mp4")
val readTypeTail = arrayOf(".ppt", ".pptx", ".doc")