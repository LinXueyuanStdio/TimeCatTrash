package com.timecat.component.bmob.dao.block

import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.ext.isCommented
import com.timecat.identity.data.base.NoteBody
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.data.block.COMMENT_SIMPLE
import com.timecat.identity.data.block.CommentBlock
import com.timecat.identity.data.block.SimpleComment
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnFindListener
import com.timecat.identity.data.service.OnSaveListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
object CommentDao {

    /**
     * user 对 to 发表 text 评论
     */
    fun addSimpleComment(
        user: _User, to: Block, text: String,
        success: ((Block) -> Unit)?,
        error: ((DataError) -> Unit)?
    ) {
        val cb = CommentBlock(
            COMMENT_SIMPLE,
            SimpleComment(
                content = NoteBody()
            ).toJson()
        )
        BlockDao.add(
            user, to, text, text,
            BLOCK_COMMENT, cb.toJson(), object : OnSaveListener<Block> {
                override fun success(data: Block) {
                    to.isCommented()
                    success?.invoke(data)
                }

                override fun error(e: DataError) {
                    e.printStackTrace()
                    error?.invoke(e)
                }
            }
        )
    }

    fun addComment(
        user: _User, comment: Block, parent: Block,
        success: ((Block) -> Unit)?,
        error: ((DataError) -> Unit)?
    ) {
        comment.type = BLOCK_COMMENT
        comment.user = user
        comment.parent = parent
        comment.tableName = "Block"
        BlockDao.save(comment, object : OnSaveListener<Block> {
            override fun success(data: Block) {
                parent.isCommented()
                success?.invoke(data)
            }

            override fun error(e: DataError) {
                e.printStackTrace()
                error?.invoke(e)
            }
        }
        )
    }

    fun findAll(
        block: Block,
        listener: OnFindListener<Block>? = null
    ) {
        BlockDao.findAll(
            listOf(
                BLOCK_COMMENT
            ), user = null, block = block, listener = listener
        )
    }
}