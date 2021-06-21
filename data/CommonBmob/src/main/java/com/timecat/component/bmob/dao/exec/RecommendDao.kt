package com.timecat.component.bmob.dao.exec

import cn.bmob.v3.BmobQuery
import com.timecat.component.bmob.dao.Block2BlockDao
import com.timecat.component.bmob.dao.ExecDao
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.data.common.Block2Block
import com.timecat.component.bmob.data.common.Exec
import com.timecat.identity.data.block_block.Block2Block_Leaderboard_has_block
import com.timecat.identity.data.exec.EXEC_Recommend
import com.timecat.identity.data.exec.RecommendBlock
import com.timecat.identity.data.exec.RecommendLeaderBoard
import com.timecat.identity.data.service.OnFindListener
import com.timecat.identity.data.service.OnSaveListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
object RecommendDao {
    /**
     * Recommend <- user, title, content, extra
     * block -> leaderBoardBlock
     */
    fun addForLeaderBoard(
        user: _User,
        title: String,
        content: String,
        extra: String,
        block: Block,
        leaderBoardBlock: Block,
        listener: OnSaveListener<Exec>? = null
    ) {
        addForLeaderBoard(
            user,
            title,
            content,
            extra,
            block,
            leaderBoardBlock.objectId,
            listener
        )
    }

    /**
     * Recommend <- user, title, content, extra
     * block -> leaderBoardId
     */
    fun addForLeaderBoard(
        user: _User,
        title: String,
        content: String,
        extra: String,
        block: Block,
        leaderBoardId: String,
        listener: OnSaveListener<Exec>? = null
    ) {
        val rl = RecommendLeaderBoard(
            blockId = block.objectId,
            targetLeaderBoardId = leaderBoardId
        )
        val rb = RecommendBlock(
            title,
            content,
            extra,
            RecommendBlock.RECOMMEND_LEADERBOARD,
            rl.toJson()
        )
        val exec = Exec.forRecommend(user)
        exec.structure = rb.toJson()
        exec.status = RecommendBlock.RECOMMEND_WAITING_TO_CHECK
        ExecDao.save(exec, listener)
    }

    fun findAll_ongoing(user: _User? = null, listener: OnFindListener<Exec>? = null) {
        findAll(
            EXEC_Recommend, user, listOf(
                RecommendBlock.RECOMMEND_WAITING_TO_CHECK,
                RecommendBlock.RECOMMEND_RECHECK
            ), listener
        )
    }

    fun findAll(
        type: Int,
        user: _User? = null,
        status: List<Long> = listOf(0),
        listener: OnFindListener<Exec>? = null
    ) = ExecDao.list(findAll(type, user, status), listener)

    fun findAll(
        type: Int,
        user: _User? = null,
        status: List<Long> = listOf(0)
    ): BmobQuery<Exec> {
        val q = BmobQuery<Exec>()
        if (user != null) q.addWhereEqualTo("user", user)
        q.addWhereEqualTo("type", type)
        q.include("user")
        q.addWhereContainedIn("status", status)
        return q
    }

    fun findAll(type: Int, user: _User? = null): BmobQuery<Block> {
        val q = BmobQuery<Block>()
        if (user != null) q.addWhereEqualTo("user", user)
        q.addWhereEqualTo("type", type)
        q.include("user,parent")
        return q
    }

    fun findAll(
        type: List<Int> = listOf(0),
        user: _User? = null,
        block: Block? = null
    ): BmobQuery<Block2Block> {
        val q = BmobQuery<Block2Block>()
        if (user != null) q.addWhereEqualTo("user", user)
        if (block != null) q.addWhereEqualTo("parent", block)
        q.include("user,parent")
        q.addWhereContainedIn("type", type)
        return q
    }

    fun findAll_finished(user: _User? = null, listener: OnFindListener<Exec>? = null) {
        findAll(
            EXEC_Recommend, user, listOf(
                RecommendBlock.RECOMMEND_ACCEPTED,
                RecommendBlock.RECOMMEND_RECHECK
            ), listener
        )
    }

    fun findAll_cancel(user: _User? = null, listener: OnFindListener<Exec>? = null) {
        findAll(
            EXEC_Recommend, user, listOf(
                RecommendBlock.RECOMMEND_CANCEL
            ), listener
        )
    }

    /**
     * 将 recommended 推荐到 recommendTo
     * 例如将 WebBlock 推荐到 LeaderBoard
     */
    fun successRecommend(
        user: _User,
        from: String,
        to: String,
        listener: OnSaveListener<Block2Block>? = null
    ) {
        val fromBlock = Block(user, 0)
        fromBlock.objectId = from
        val toBlock = Block(user, 0)
        toBlock.objectId = to
        val l = Block2Block(user, fromBlock, toBlock, Block2Block_Leaderboard_has_block)
        Block2BlockDao.save(l, listener)
    }
}