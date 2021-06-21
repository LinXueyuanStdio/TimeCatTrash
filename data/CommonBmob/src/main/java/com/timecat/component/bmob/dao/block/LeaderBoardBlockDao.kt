package com.timecat.component.bmob.dao.block

import com.timecat.component.bmob.dao.Block2BlockDao
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.data.common.Block2Block
import com.timecat.identity.data.block.type.BLOCK_LEADER_BOARD
import com.timecat.identity.data.block.ListBlock
import com.timecat.identity.data.block_block.Block2Block_Leaderboard_has_block
import com.timecat.identity.data.service.OnFindListener
import com.timecat.identity.data.service.OnSaveListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
object LeaderBoardBlockDao {
    fun add(
        user: _User,
        title: String,
        content: String,
        list: ListBlock,
        listener: OnSaveListener<Block>? = null
    ) {
        BlockDao.add(
            user, title, content,
            BLOCK_LEADER_BOARD, list.toJson(), listener
        )
    }

    fun findAll(user: _User? = null, listener: OnFindListener<Block>? = null) {
        BlockDao.findAll(BLOCK_LEADER_BOARD, user, listener)
    }

    fun findAllItemsInBoard(block: Block, listener: OnFindListener<Block2Block>? = null) {
        Block2BlockDao.list(Block2BlockDao.findAllChild(block, Block2Block_Leaderboard_has_block), listener)
    }

    fun delete(block: Block) {
        BlockDao.delete(block)
    }
}