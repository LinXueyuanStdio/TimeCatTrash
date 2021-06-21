package com.timecat.component.bmob.data.common

import cn.bmob.v3.BmobObject
import com.timecat.component.bmob.data._User
import com.timecat.identity.data.trace.TraceType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
class Trace(
    var user: _User,
    @TraceType var type: Int = 0,
    var targetType: Int = 0,
    var targetId: String = "",
    var structure: String = "",
) : BmobObject("Trace"), Serializable {
    init {
        tableName = "Trace"
    }
}