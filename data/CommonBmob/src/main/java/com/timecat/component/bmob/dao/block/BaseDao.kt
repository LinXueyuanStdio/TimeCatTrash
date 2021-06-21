package com.timecat.component.bmob.dao.block

import com.timecat.component.bmob.data.common.Block

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 基本的增删改查接口
 * @usage 出于设计性，不支持删除和修改，只允许增加和查找
 */
abstract class BaseDao(type: Int) {
    abstract fun findAll(res: (List<Block>) -> Unit)
    abstract fun findOne(id:String, res: (Block) -> Unit)
    fun findChildren(id:String, res: (List<Block>) -> Unit){}
}