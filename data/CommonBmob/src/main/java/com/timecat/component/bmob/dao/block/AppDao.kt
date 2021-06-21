package com.timecat.component.bmob.dao.block

import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.BLOCK_APP
import com.timecat.identity.data.service.OnFindListener
import com.timecat.identity.data.service.OnSaveListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
object AppDao {

    fun getAllPluginApp(listener: OnFindListener<Block>? = null) {
        BlockDao.findAll(
            listOf(BLOCK_APP), listOf(BLOCK_APP_Plugin),
            null, null, listener
        )
    }

    fun addWebApp(
        user: _User,
        title: String, content: String, url: String,
        listener: OnSaveListener<Block>? = null
    ) {
        val webApp = WebApp(
            url,
            mutableListOf(
                "http://xichen.pub/TimeCatOSS/preface/preview_0.webp"
            )
        )
        val appBlock = AppBlock(
            APP_WebApp,
            webApp.toJson()
        )
        addApp(
            user, title, content,
            BLOCK_APP_WebApp, appBlock.toJson(),
            null, listener
        )
    }

    fun addApp(
        user: _User, title: String, content: String,
        subType: Int, structure: String,
        parent: Block? = null,
        listener: OnSaveListener<Block>? = null
    ) {
        val b = Block(
            user,
            title = title,
            content = content,
            type = BLOCK_APP,
            subtype = subType,
            structure = structure,
            parent = parent
        )
        b.save(object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
                if (p1 == null) {
                    listener?.success(b)
                }
            }
        })
    }
}