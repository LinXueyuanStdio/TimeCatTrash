package com.timecat.component.bmob.data.common

import cn.bmob.v3.BmobObject
import com.alibaba.fastjson.JSONObject
import com.timecat.component.bmob.data._User
import com.timecat.identity.data.base.IStatus
import com.timecat.identity.data.base.Json
import com.timecat.identity.data.base.PrivacyScope
import com.timecat.identity.data.block.type.*
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description 价值：笔记、任务、习惯、计划
 * @usage
 *   type 必须线性增长，当 View 渲染大于某个数的 type 表示当前版本过低，收到较高版本，需要用户升级
 *   status 因 type 的不同而不同 写在 TaskHeader 里查询不方便，因为很多都有 delete 状态
 */
data class Block(
    var user: _User,
    @BlockType
    var type: Int,
    var subtype: Int = 0,

    var title: String = "",
    var content: String = "",

    var structure: String = "{}",
    var status: Long = 0,//不同类型 type 对 status 的解释不同
    /**
     * 点赞数
     */
    var likes: Int = 0,
    /**
     * 评论数
     */
    var comments: Int = 0,
    /**
     * 收藏数
     */
    var stars: Int = 0,
    /**
     * 转发数
     */
    var relays: Int = 0,
    /**
     * 关注数
     */
    var followers: Int = 0,
    /**
     * 浏览量
     */
    var usedBy: Int = 0, // 纯计数，单调递增 浏览量
    /**
     * 排序
     */
    var order: Int = 0,
    /**
     * 隐私域
     */
    var privacy: PrivacyScope = PrivacyScope(isPrivate = true),
    /**
     * 扩展
     */
    var ext: Json = Json(JSONObject()),
    /**
     * Block 父节点
     */
    var parent: Block? = null
) : BmobObject("Block"), Serializable, IStatus {

    companion object {
        @JvmOverloads
        fun forName(user: _User, type: Int, name: String = ""): Block {
            return Block(user, type, title = name, content = name)
        }

        fun forMoment(user: _User, content: String): Block {
            return forName(user, BLOCK_MOMENT, content)
        }
        fun forPost(user: _User, content: String): Block {
            return forName(user, BLOCK_POST, content)
        }

        fun forComment(user: _User, content: String): Block {
            return forName(user, BLOCK_COMMENT, content)
        }

        fun forApp(user: _User, content: String): Block {
            return forName(user, BLOCK_APP, content)
        }
    }

    init {
        tableName = "Block"
    }

    fun isComment(): Boolean = type == BLOCK_COMMENT

    fun isApp(): Boolean = type == BLOCK_APP

    fun getCreatedDateTime(): DateTime = DateTime(createdAt)
    fun getUpdatedDateTime(): DateTime = DateTime(updatedAt)

    fun eq(other: Block): Boolean {
        return other.objectId == objectId
    }

    //region IStatus 用 16 进制管理状态
    /**
     * 往状态集中加一个状态
     * @param status status
     */
    override fun addStatus(status: Long) {
        this.status = this.status or status
    }

    /**
     * 往状态集中移除一个状态
     * @param status status
     */
    override fun removeStatus(status: Long) {
        this.status = this.status and status.inv()
    }

    /**
     * 状态集中是否包含某状态
     * @param status status
     */
    override fun isStatusEnabled(status: Long): Boolean {
        return this.status and status != 0L
    }

    override fun statusDescription(): String = ""
    //endregion

    override fun toString(): String {
        return "$objectId(type=$type, subtype=$subtype, title=$title, content=$content,\n" +
                "         structure='$structure', status=${statusDescription()}, likes=$likes, comments=$comments, relays=$relays, usedBy=$usedBy, order=$order,\n" +
                "         parent=${parent?.objectId}, user=${user?.objectId})\n"
    }
}

