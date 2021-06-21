package com.timecat.component.bmob.data.common

import cn.bmob.v3.BmobObject
import com.timecat.component.bmob.data._User
import com.timecat.identity.data.block_block.Block2BlockType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
data class Block2Block(
    var user: _User,
    var from: Block,
    var to: Block,
    @Block2BlockType
    var type: Int = 0,
    var structure: String = "",
    var status: Long = 0
) : BmobObject("Block2Block"), Serializable{

    init {
        tableName = "Block2Block"
    }

    override fun toString(): String {
        return "$objectId(type=$type, user=${user.nick}, structure='$structure', status=$status, \n" +
                "        from=$from," +
                "        to=$to)"
    }


}

