package com.timecat.component.bmob.dao

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.CountListener
import com.timecat.component.bmob.dao.api.OnCountListener
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.data.common.Block2Block

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
object StatisticDao {
    fun getFocusBlockSum(b: _User, onDaoCountListener: OnCountListener) {
        val query = BmobQuery<Block>()
        query.addWhereRelatedTo("focusBlock", BmobPointer(b))
        query.count(Block::class.java, object : CountListener() {
            override fun done(integer: Int?, e: BmobException?) {
                if (e == null) {
                    onDaoCountListener.onSuccess(integer!!)
                }
            }
        })
    }

    fun getChildBlockSum(b: Block, onDaoCountListener: OnCountListener) {
        val query = BmobQuery<Block>()
        query.addWhereRelatedTo("child", BmobPointer(b))
        query.count(Block::class.java, object : CountListener() {
            override fun done(integer: Int?, e: BmobException?) {
                if (e == null) {
                    onDaoCountListener.onSuccess(integer!!)
                }
            }
        })
    }

    fun getTagBlockSum(b: Block, onDaoCountListener: OnCountListener) {
        val query = BmobQuery<Block2Block>()
        query.addWhereEqualTo("from", b)
        query.count(Block2Block::class.java, object : CountListener() {
            override fun done(integer: Int?, e: BmobException?) {
                if (e == null) {
                    onDaoCountListener.onSuccess(integer!!)
                }
            }
        })
    }

    fun getLikeSum(b: Block, onDaoCountListener: OnCountListener) {
        val q = BmobQuery<Block>()
        q.addWhereEqualTo("block", b)
        q.count(Block::class.java, object : CountListener() {
            override fun done(integer: Int?, e: BmobException?) {
                if (e == null) {
                    onDaoCountListener.onSuccess(integer!!)
                }
            }
        })
    }

    fun getCommentSum(b: Block, onDaoCountListener: OnCountListener) {
        val q = BmobQuery<Block>()
        q.addWhereEqualTo("parent", b)
        q.count(Block::class.java, object : CountListener() {
            override fun done(integer: Int?, e: BmobException?) {
                if (e == null) {
                    onDaoCountListener.onSuccess(integer!!)
                }
            }
        })
    }

}