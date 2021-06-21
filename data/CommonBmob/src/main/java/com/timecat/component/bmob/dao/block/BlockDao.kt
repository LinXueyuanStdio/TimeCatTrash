package com.timecat.component.bmob.dao.block

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.ext.toDataError
import com.timecat.identity.data.block.BlockMini
import com.timecat.identity.data.block.type.BlockType
import com.timecat.identity.data.service.OnFindListener
import com.timecat.identity.data.service.OnSaveListener
import com.timecat.identity.data.service.OnUpdateListener
import io.reactivex.Observable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description Dao for Block
 * @usage null
 */
object BlockDao {
    fun fromSync(list: List<BlockMini>): List<Block> {
        val ids = list.map { it.id }
        val q = BmobQuery<Block>()
        q.include("user,parent")
        q.addWhereContainedIn("objectId", ids)
        return q.findObjectsSync(Block::class.java)
    }

    fun from(list: List<BlockMini>, listener: OnFindListener<Block>? = null) {
        val ids = list.map { it.id }
        val q = BmobQuery<Block>()
        q.include("user,parent")
        q.addWhereContainedIn("objectId", ids)
        list(q, listener)
    }

    fun find(id: String, listener: OnFindListener<Block>? = null) {
        val q = BmobQuery<Block>()
        q.addWhereEqualTo("objectId", id)
        q.include("user,parent")
        q.setLimit(1)
        list(q, listener)
    }

    fun find(id: String): Observable<MutableList<Block>>? {
        val q = BmobQuery<Block>()
        q.addWhereEqualTo("objectId", id)
        q.include("user,parent")
        q.setLimit(1)
        return q.findObjectsObservable(Block::class.java)
    }

    fun findByMini(b: BlockMini, listener: OnFindListener<Block>? = null) = find(b.id, listener)

    fun findByMini(b: BlockMini): Observable<MutableList<Block>>? = find(b.id)

    fun add(
        user: _User,
        block: Block?,
        title: String,
        content: String,
        @BlockType type: Int,
        structure: String,
        listener: OnSaveListener<Block>? = null
    ) {
        val b = Block(
            user,
            title = title,
            content = content,
            type = type,
            structure = structure,
            parent = block,
            status = 0
        )
        b.save(object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
                if (p1 == null) {
                    listener?.success(b)
                }
            }
        })
    }

    fun add(
        user: _User,
        title: String,
        content: String,
        @BlockType type: Int,
        structure: String,
        listener: OnSaveListener<Block>? = null
    ) {
        add(user, null, title, content, type, structure, listener)
    }

    fun findAll(
        type: Int,
        user: _User? = null,
        status: List<Long> = listOf(0),
        listener: OnFindListener<Block>? = null
    ) {
        list(findAll(type, user, status), listener)
    }

    fun findAll(
        type: Int,
        user: _User? = null,
        status: List<Long> = listOf(0)
    ): BmobQuery<Block> {
        val q = BmobQuery<Block>()
        if (user != null) q.addWhereEqualTo("user", user)
        q.addWhereEqualTo("type", type)
        q.include("user,parent")
        q.addWhereContainedIn("status", status)
        return q
    }

    fun findAll(type: Int, user: _User? = null, listener: OnFindListener<Block>? = null) {
        list(findAll(type, user), listener)
    }

    fun findAll(type: Int, user: _User? = null): BmobQuery<Block> {
        val q = BmobQuery<Block>()
        if (user != null) q.addWhereEqualTo("user", user)
        q.addWhereEqualTo("type", type)
        q.order("-updatedAt")
        q.include("user,parent")
        return q
    }

    fun findAll(
        type: List<Int> = listOf(0),
        user: _User? = null,
        block: Block? = null,
        listener: OnFindListener<Block>? = null
    ) = list(findAll(type, user, block), listener)


    fun findAll(
        type: List<Int> = listOf(0),
        user: _User? = null,
        block: Block? = null
    ): BmobQuery<Block> {
        val q = BmobQuery<Block>()
        if (user != null) q.addWhereEqualTo("user", user)
        if (block != null) q.addWhereEqualTo("parent", block)
        q.include("user,parent")
        q.addWhereContainedIn("type", type)
        return q
    }

    fun findAll(
        type: List<Int> = listOf(0),
        subType: List<Int> = listOf(0),
        user: _User? = null,
        block: Block? = null,
        listener: OnFindListener<Block>? = null
    ) = list(findAll(type, subType, user, block), listener)


    fun findAll(
        type: List<Int> = listOf(0),
        subType: List<Int> = listOf(0),
        user: _User? = null,
        block: Block? = null
    ): BmobQuery<Block> {
        val q = BmobQuery<Block>()
        if (user != null) q.addWhereEqualTo("user", user)
        if (block != null) q.addWhereEqualTo("parent", block)
        q.include("user,parent")
        q.addWhereContainedIn("type", type)
        q.addWhereContainedIn("subType", subType)
        return q
    }

    fun delete(block: Block, listener: OnUpdateListener<Block>? = null) {
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

    fun list(q: BmobQuery<Block>, listener: OnFindListener<Block>? = null) {
        q.findObjects(object : FindListener<Block>() {
            override fun done(p0: MutableList<Block>?, e: BmobException?) {
                if (e == null) {
                    listener?.success(p0!!)
                } else {
                    listener?.error(e.toDataError())
                }
            }
        })
    }

    fun save(block: Block, listener: OnSaveListener<Block>?) {
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