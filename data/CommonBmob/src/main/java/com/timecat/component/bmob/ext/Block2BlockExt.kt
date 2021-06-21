package com.timecat.component.bmob.ext

import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.data.common.Block2Block
import com.timecat.identity.data.block_block.Block2BlockType
import com.timecat.identity.data.block_block.Block2Block_Identity_has_role
import com.timecat.identity.data.block_block.Block2Block_Role_has_permission

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/4
 * @description null
 * @usage null
 */
fun relation(
    author: _User,
    source: Block,
    target: Block,
    @Block2BlockType type: Int
) = Block2Block(author, source, target, type, "", 0)

infix fun _User.let_Identity_has_role(identity_to_role: Pair<Block, Block>): Block2Block {
    return relation(this, identity_to_role.first, identity_to_role.second, Block2Block_Identity_has_role)
}
infix fun _User.let_Role_has_permission(role_to_permission: Pair<Block, Block>): Block2Block {
    return relation(this, role_to_permission.first, role_to_permission.second, Block2Block_Role_has_permission)
}
