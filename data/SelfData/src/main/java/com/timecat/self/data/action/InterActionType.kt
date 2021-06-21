package com.timecat.self.data.action

import androidx.annotation.IntDef
import com.timecat.self.data.block.type.BLOCK_IDENTITY
import com.timecat.self.data.block.type.BLOCK_PERMISSION
import com.timecat.self.data.block.type.BLOCK_RECOMMEND
import com.timecat.self.data.block.type.BLOCK_ROLE

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description null
 * @usage null
 */
@IntDef(
    INTERACTION_Recommend,
    INTERACTION_Auth_Permission,
    INTERACTION_Auth_Role,
    INTERACTION_Auth_Identity
)
@Retention(AnnotationRetention.SOURCE)
annotation class InterActionType

const val INTERACTION_Recommend: Int = BLOCK_RECOMMEND // 推荐
const val INTERACTION_Auth_Permission: Int = BLOCK_PERMISSION // 授予权限
const val INTERACTION_Auth_Role: Int = BLOCK_ROLE // 授予角色
const val INTERACTION_Auth_Identity: Int = BLOCK_IDENTITY // 授予身份
