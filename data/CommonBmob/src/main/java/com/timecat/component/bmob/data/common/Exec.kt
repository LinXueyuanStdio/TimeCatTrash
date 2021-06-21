package com.timecat.component.bmob.data.common

import cn.bmob.v3.BmobObject
import com.timecat.component.bmob.data._User
import com.timecat.identity.data.exec.EXEC_Recommend
import com.timecat.identity.data.exec.ExecType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-15
 * @description 用户行为
 * @usage null
 */
class Exec(
    var user: _User,
    @ExecType var type: Int = 0,
    var structure: String = "",
    var status: Long = 0
) : BmobObject("Exec"), Serializable {
    companion object {
        fun forRecommend(user: _User): Exec {
            return Exec(user, EXEC_Recommend)
        }
    }

    init {
        tableName = "Exec"
    }

    override fun toString(): String {
        return "${user.objectId} : $type, $status : $structure"
    }
}