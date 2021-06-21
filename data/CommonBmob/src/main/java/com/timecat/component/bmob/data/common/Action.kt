package com.timecat.component.bmob.data.common

import cn.bmob.v3.BmobObject
import com.timecat.component.bmob.data._User
import com.timecat.identity.data.action.ActionType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description 动作：投票、点赞、打卡、时间块记录
 * Action 的目标是 Block，不是 Action。
 * @usage
 *   status 因 type 的不同而不同，很多都有 delete 状态
 */
data class Action(
    var user: _User,
    var block: Block? = null,
    @ActionType var type: Int = 0,
    var structure: String = "",
    var status: Long = 0
    //var like: Int, // 动作的点赞数，如坚持打卡的点赞数
) : BmobObject("Action"), Serializable{
    init {
        tableName = "Action"
    }
}

