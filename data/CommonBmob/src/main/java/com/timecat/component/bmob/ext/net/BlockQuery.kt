package com.timecat.component.bmob.ext.net

import cn.bmob.v3.BmobQuery
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
//region all block (by  type)
fun allMoment() = allBlockByType(BLOCK_MOMENT)
fun allForum() = allBlockByType(BLOCK_FORUM)
fun allTopic() = allBlockByType(BLOCK_TOPIC)
fun allTag() = allBlockByType(BLOCK_TAG)
fun allRole() = allBlockByType(BLOCK_ROLE)
fun allIdentity() = allBlockByType(BLOCK_IDENTITY)
fun allPermission() = allBlockByType(BLOCK_PERMISSION)
fun allMetaPermission() = allPermission().apply {
    addWhereEqualTo("subtype", PERMISSION_Meta)
}
fun allHunPermission() = allPermission().apply {
    addWhereEqualTo("subtype", PERMISSION_Hun)
}
fun allBlockByType(type: Int) = BmobQuery<Block>().apply {
    addWhereEqualTo("type", type)
    include("user,parent")
    order("-createdAt")
    setLimit(200)
}

fun allBlock() = BmobQuery<Block>().apply {
    include("user,parent")
    order("-createdAt")
    setLimit(200)
}
//endregion

//region all block (by  type) for user
fun _User.allMoment() = allBlockByType(BLOCK_MOMENT)
fun _User.allForum() = allBlockByType(BLOCK_FORUM)
fun _User.allTopic() = allBlockByType(BLOCK_TOPIC)
fun _User.allTag() = allBlockByType(BLOCK_TAG)
fun _User.allRole() = allBlockByType(BLOCK_ROLE)
fun _User.allIdentity() = allBlockByType(BLOCK_IDENTITY)
fun _User.allPermission() = allBlockByType(BLOCK_PERMISSION)
fun _User.allMetaPermission() = allPermission().apply {
    addWhereEqualTo("subtype", PERMISSION_Meta)
}
fun _User.allHunPermission() = allPermission().apply {
    addWhereEqualTo("subtype", PERMISSION_Hun)
}
fun _User.allBlockByType(type: Int) = BmobQuery<Block>().apply {
    addWhereEqualTo("user", this@allBlockByType)
    addWhereEqualTo("type", type)
    include("user,parent")
    order("-createdAt")
    setLimit(200)
}

fun _User.allBlock() = BmobQuery<Block>().apply {
    include("user,parent")
    order("-createdAt")
    setLimit(200)
}
//endregion

fun oneBlockOf(id: String) = BmobQuery<Block>().apply {
    addWhereEqualTo("objectId", id)
    order("-createdAt")
    include("user,parent")
    setLimit(1)
}

//region children of block
fun childrenOf(block: Block, type: List<Int>) = BmobQuery<Block>().apply {
    addWhereEqualTo("parent", block)
    order("-createdAt")
    include("user,parent")
    addWhereContainedIn("type", type)
}
fun childrenOf(blocks: List<Block>, type: List<Int>) = BmobQuery<Block>().apply {
    addWhereContainedIn("parent", blocks)
    order("-createdAt")
    include("user,parent")
    addWhereContainedIn("type", type)
}

fun Block.findAllPost() = childrenOf(this, listOf(BLOCK_POST))
fun Block.findAllMoment() = childrenOf(this, listOf(BLOCK_MOMENT))
fun Block.findAllComment() = childrenOf(this, listOf(BLOCK_COMMENT))
//endregion

fun checkBlockExistByTitle(title:String, type:Int): BmobQuery<Block> = BmobQuery<Block>().apply {
    addWhereEqualTo("title", title)
    order("-createdAt")
    addWhereEqualTo("type", type)
}
fun checkTagExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_TAG)
fun checkTopicExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_TOPIC)
fun checkForumExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_FORUM)
fun checkPermissionExistByTitle(title: String, subtype:Int) = checkBlockExistByTitle(title, BLOCK_PERMISSION).apply {
    addWhereEqualTo("subtype", subtype)
}
fun checkMetaPermExistByTitle(title: String) = checkPermissionExistByTitle(title, PERMISSION_Meta)
fun checkHunPermExistByTitle(title: String) = checkPermissionExistByTitle(title, PERMISSION_Hun)
fun checkRoleExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_ROLE)
fun checkIdentityExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_IDENTITY)
