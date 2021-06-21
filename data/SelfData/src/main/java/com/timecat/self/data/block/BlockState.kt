package com.timecat.self.data.block

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-24
 * @description Block 通用等状态属性，每个状态拥有对应的时间字段
 * @usage null
 */
//以下每个都拥有对应的时间字段
const val BLOCK_DELETE: Long = 0x0001//是否删除
const val BLOCK_READ_ONLY: Long = 0x0002//是否只读，对所有者也只读
const val BLOCK_ARCHIVE: Long = 0x0004//是否归档，可以删除，不能修改
const val BLOCK_LOCK: Long = 0x0008//是否锁定，不能删除，不能修改
const val BLOCK_BLOCK: Long = 0x0010//是否封印，不能查看，不能删除，不能修改

//以下对应的时间字段合并为一个，权限修改时间
const val BLOCK_PRIVATE: Int = 1//私有
const val BLOCK_PUBLIC: Int= 2//公有
const val BLOCK_PROTECT: Int= 3//保护