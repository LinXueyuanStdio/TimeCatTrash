package com.timecat.component.bmob.dao

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.timecat.component.bmob.data.common.Exec
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
object ExecDao {

    fun delete(block: Exec, listener: OnUpdateListener<Exec>? = null) {
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

    fun list(q: BmobQuery<Exec>, listener: OnFindListener<Exec>? = null) {
        q.findObjects(object : FindListener<Exec>() {
            override fun done(p0: MutableList<Exec>?, e: BmobException?) {
                if (e == null) {
                    listener?.success(p0!!)
                } else {
                    listener?.error(e.toDataError())
                }
            }
        })
    }

    fun save(block: Exec, listener: OnSaveListener<Exec>?) {
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

    fun update(block: Exec, listener: OnUpdateListener<Exec>?) {
        block.update(object : UpdateListener() {
            override fun done(e: BmobException?) {
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