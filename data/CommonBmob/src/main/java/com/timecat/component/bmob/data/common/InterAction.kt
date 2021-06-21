package com.timecat.component.bmob.data.common

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobDate
import com.timecat.component.bmob.data._User
import com.timecat.identity.data.action.InterActionType
import org.joda.time.DateTime
import java.io.Serializable
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description 两个用户的交互动作，该动作涉及一个块
 * 用户A，C
 * 块 B
 * 举例：
 * A 将 B 推荐给 C
 * A 将 B 赠送给 C
 * A 将 B 分配给 C
 * A 将 B 授予给 C（授权）
 * @usage
 *   status 因 type 的不同而不同，很多都有 delete 状态
 */
data class InterAction(
    //动作发起者
    var user: _User,
    //分配块
    var block: Block,
    //动作接收者
    var target: _User,
    @InterActionType
    var type: Int,
    var activeTime: BmobDate = BmobDate(Date()),
    var expireTime: BmobDate = BmobDate(Date()),
    var structure: String = "",
    var status: Long = 0
) : BmobObject("InterAction"), Serializable {
    init {
        tableName = "InterAction"
    }

    var activeDateTime: DateTime
        get() = DateTime(activeTime.date)
        set(value) {
            activeTime = BmobDate(value.toDate())
        }
    var expireDateTime: DateTime
        get() = DateTime(expireTime.date)
        set(value) {
            expireTime = BmobDate(value.toDate())
        }

    override fun toString(): String {
        return "$objectId(type=$type, activeTime=$activeTime, expireTime=$expireTime, structure='$structure', status=$status\n"+
                "         user=${user?.objectId}, \n" +
                "         block=$block" +
                "         target=${target?.objectId})\n"
    }


}

