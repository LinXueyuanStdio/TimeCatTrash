package com.timecat.component.bmob.dao

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.data.common.Block2Block
import com.timecat.component.bmob.ext.toDataError
import com.timecat.identity.data.service.OnFindListener
import com.timecat.identity.data.service.OnSaveListener
import com.timecat.identity.data.service.OnUpdateListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
object Block2BlockDao {

    fun findAllChild(block: Block, type: Int, listener: OnFindListener<Block2Block>? = null) {
        list(findAllChild(block, type), listener)
    }

    fun findAllChild(block: Block, type: Int): BmobQuery<Block2Block> {
        val q = BmobQuery<Block2Block>()
        q.addWhereEqualTo("to", block)
        q.addWhereEqualTo("type", type)
        q.include("from,to")
        return q
    }

    fun exist(user: _User, from: String, to: String, type: Int): BmobQuery<Block2Block> {
        val q = BmobQuery<Block2Block>()
        val fromBlock = Block(user, 0)
        fromBlock.objectId = from
        val toBlock = Block(user, 0)
        toBlock.objectId = to
        q.addWhereEqualTo("from", fromBlock)
        q.addWhereEqualTo("to", toBlock)
        q.addWhereEqualTo("type", type)
        q.include("from,to")
        return q
    }

    fun delete(block: Block2Block, listener: OnUpdateListener<Block2Block>? = null) {
        block.delete(object : UpdateListener() {
            override fun done(e: BmobException?) {
                if (e == null) {
                    listener?.success(block)
                } else {
                    listener?.error(e.toDataError())
                }
            }
        })
    }

    fun list(q: BmobQuery<Block2Block>, listener: OnFindListener<Block2Block>? = null) {
        q.findObjects(object : FindListener<Block2Block>() {
            override fun done(p0: MutableList<Block2Block>?, e: BmobException?) {
                if (e == null) {
                    listener?.success(p0!!)
                } else {
                    listener?.error(e.toDataError())
                }
            }
        })
    }

    fun save(block: Block2Block, listener: OnSaveListener<Block2Block>?) {
        block.save(object : SaveListener<String>() {
            override fun done(id: String?, e: BmobException?) {
                when {
                    e == null -> {
                        listener?.success(block)
                    }
                    else -> {
                        listener?.error(e.toDataError())
                    }
                }
            }
        })
    }
}