package com.timecat.component.bmob.ext.net

import cn.bmob.v3.BmobQuery
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.data.common.User2User
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.identity.data.block.type.BLOCK_POST
import com.timecat.identity.data.user_user.User2User_Ding
import com.timecat.identity.data.user_user.User2User_Follow
import com.timecat.identity.data.user_user.User2User_Like
import com.timecat.identity.data.user_user.User2User_Score

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
fun oneUserRelationOf(id: String) = BmobQuery<User2User>().apply {
    addWhereEqualTo("objectId", id)
    include("user,target")
    order("-createdAt")
    setLimit(1)
}

fun _User.allRelationByType(
    type: Int,
    target: _User? = null
): BmobQuery<User2User> {
    val q = BmobQuery<User2User>()
    q.addWhereEqualTo("author", this)
    q.addWhereEqualTo("type", type)
    q.order("-createdAt")
    if (target != null) q.addWhereEqualTo("target", target)
    return q
}
fun allRelationOf(
    target: _User,
    type: Int
): BmobQuery<User2User> {
    val q = BmobQuery<User2User>()
    q.addWhereEqualTo("type", type)
    q.order("-createdAt")
    q.addWhereEqualTo("target", target)
    return q
}

fun _User.allLike(target: _User? = null) = allRelationByType(User2User_Like, target)
fun _User.allDing(target: _User? = null) = allRelationByType(User2User_Ding, target)
fun _User.allScore(target: _User? = null) = allRelationByType(User2User_Score, target)
fun _User.allFollow(target: _User? = null) = allRelationByType(User2User_Follow, target)
fun fansOf(user: _User) = allRelationOf(user, User2User_Follow)


fun childrenOf(user: _User, type: List<Int>) = BmobQuery<Block>().apply {
    addWhereEqualTo("user", user)
    include("user,parent")
    order("-createdAt")
    addWhereContainedIn("type", type)
}

fun _User.findAllPost() = childrenOf(this, listOf(BLOCK_POST))
fun _User.findAllMoment() = childrenOf(this, listOf(BLOCK_MOMENT))
fun _User.findAllComment() = childrenOf(this, listOf(BLOCK_COMMENT))
