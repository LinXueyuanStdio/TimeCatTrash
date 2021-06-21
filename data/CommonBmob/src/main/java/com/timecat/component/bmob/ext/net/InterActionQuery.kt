package com.timecat.component.bmob.ext.net

import cn.bmob.v3.BmobQuery
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.data.common.InterAction
import com.timecat.identity.data.action.INTERACTION_Auth_Identity
import com.timecat.identity.data.action.INTERACTION_Auth_Permission
import com.timecat.identity.data.action.INTERACTION_Auth_Role
import com.timecat.identity.data.action.INTERACTION_Recommend

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/22
 * @description null
 * @usage null
 */

fun _User.allInterActionByType(
    type: List<Int>,
    block: Block? = null
): BmobQuery<InterAction> {
    val q = BmobQuery<InterAction>()
    q.addWhereEqualTo("user", this)
    if (block != null) q.addWhereEqualTo("block", block)
    q.addWhereContainedIn("type", type)
    q.order("-createdAt")
    q.include("user,block,target")
    return q
}

fun _User.allInterActionByOneType(type:Int, block: Block? = null) = allInterActionByType(listOf(type), block)
fun _User.allRecommendBlockToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Recommend, block)
fun _User.allAuthRoleToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Role, block)
fun _User.allAuthIdentityToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Identity, block)
fun _User.allAuthPermissionToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Permission, block)
fun _User.allAuthToSomeone(block: Block? = null) = allInterActionByType(listOf(
    INTERACTION_Auth_Identity, INTERACTION_Auth_Role, INTERACTION_Auth_Permission
), block)

fun _User.allInterActionTargetedByType(
    type: Int,
    block: Block? = null
): BmobQuery<InterAction> {
    val q = BmobQuery<InterAction>()
    q.addWhereEqualTo("target", this)
    if (block != null) q.addWhereEqualTo("block", block)
    q.addWhereEqualTo("type", type)
    q.order("-createdAt")
    q.include("user,block,target")
    return q
}

fun _User.allInterActionTargetedByType(
    type: List<Int>,
    block: Block? = null
): BmobQuery<InterAction> {
    val q = BmobQuery<InterAction>()
    q.addWhereEqualTo("target", this)
    if (block != null) q.addWhereEqualTo("block", block)
    q.addWhereContainedIn("type", type)
    q.order("-createdAt")
    q.include("user,block,target")
    return q
}

/**
 * 被授予的角色
 */
fun _User.allTargetedByAuthRole(block: Block? = null) = allInterActionTargetedByType(INTERACTION_Auth_Role, block)

/**
 * 被授予的身份
 */
fun _User.allTargetedByAuthIdentity(block: Block? = null) =
    allInterActionTargetedByType(INTERACTION_Auth_Identity, block)

/**
 * 被授予的权限
 */
fun _User.allTargetedByAuthPermission(block: Block? = null) =
    allInterActionTargetedByType(INTERACTION_Auth_Permission, block)

fun _User.allTargetedByAuth(block: Block? = null) = allInterActionTargetedByType(
    listOf(
        INTERACTION_Auth_Permission,
        INTERACTION_Auth_Role,
        INTERACTION_Auth_Identity
    ), block
)

fun Block.allInterActionByType(
    type: Int,
    user: _User? = null,
    target: _User? = null
): BmobQuery<InterAction> {
    val q = BmobQuery<InterAction>()
    q.addWhereEqualTo("block", this)
    q.order("-createdAt")
    q.addWhereEqualTo("type", type)
    if (user != null) q.addWhereEqualTo("user", user)
    if (target != null) q.addWhereEqualTo("target", target)
    return q
}

fun Block.asRole_allAuthAction(user: _User? = null, target: _User? = null) =
    allInterActionByType(INTERACTION_Auth_Role, user, target)

fun _User.allInterAction(): BmobQuery<InterAction> {
    val q = BmobQuery<InterAction>()
    q.addWhereEqualTo("user", this)
    q.order("-createdAt")
    return q
}