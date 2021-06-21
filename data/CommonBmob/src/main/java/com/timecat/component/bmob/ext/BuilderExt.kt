package com.timecat.component.bmob.ext

import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.identity.data.block.ForumBlock
import com.timecat.identity.data.block.TagBlock
import com.timecat.identity.data.block.TopicBlock
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
infix fun _User.like(block: Block) = UserBlockPack(this, block)

//region 创建一个次级块，和父块相关
class UserBlockPack(
    var user: _User,
    var block: Block
) {
    fun build(b: BlockBuilder): Block {
        return Block(
            user,
            type = b.type,
            subtype = b.subtype,
            parent = block
        ).apply {
            title = b.title
            content = b.content
            tableName = b.tableName
        }
    }
}

infix fun _User.commentsOn(parent: Block) = UserBlockPack(this, parent)
infix fun UserBlockPack.with(builder: CommentBuilder): Block {
    return build(builder)
}

infix fun _User.postsOn(parent: Block) = UserBlockPack(this, parent)
infix fun UserBlockPack.with(builder: PostBuilder): Block {
    return build(builder)
}
//endregion

//region 创建一个顶级块，和其他块无关
infix fun _User.createBlock(builder: BlockBuilder): Block {
    return Block(
        this,
        type = builder.type,
        subtype = builder.subtype,
        title = builder.title,
        content = builder.content
    ).apply {
        tableName = builder.tableName
    }
}

infix fun _User.create(builder: ForumBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun _User.create(builder: TopicBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun _User.create(builder: TagBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun _User.create(builder: PermissionBuilder): Block {
    return createBlock(builder)
}
infix fun _User.create(builder: RoleBuilder): Block {
    return createBlock(builder)
}
infix fun _User.create(builder: IdentityBuilder): Block {
    return createBlock(builder)
}
//endregion

//region BlockBuilder
abstract class BlockBuilder(
    var type: Int,
    var subtype: Int = 0,
    var tableName: String = "Block"
) {
    var title: String = ""
    var content: String = ""
}

class CommentBuilder : BlockBuilder(BLOCK_COMMENT)

fun Comment(create: CommentBuilder.() -> Unit) = CommentBuilder().apply(create)

class PostBuilder : BlockBuilder(BLOCK_POST)

fun Post(create: PostBuilder.() -> Unit) = PostBuilder().apply(create)

class ForumBuilder : BlockBuilder(BLOCK_FORUM) {
    var headerBlock: ForumBlock = ForumBlock()
}

fun Forum(create: ForumBuilder.() -> Unit) = ForumBuilder().apply(create)

class TopicBuilder : BlockBuilder(BLOCK_TOPIC){
    var headerBlock : TopicBlock = TopicBlock()
}

fun Topic(create: TopicBuilder.() -> Unit) = TopicBuilder().apply(create)

class TagBuilder : BlockBuilder(BLOCK_TAG) {
    var headerBlock : TagBlock = TagBlock()
}

fun Tag(create: TagBuilder.() -> Unit) = TagBuilder().apply(create)


class PermissionBuilder(subtype: Int = 0) : BlockBuilder(BLOCK_PERMISSION, subtype)

fun Permission(create: PermissionBuilder.() -> Unit) = PermissionBuilder().apply(create)
fun HunPermission(create: PermissionBuilder.() -> Unit) = PermissionBuilder(PERMISSION_Hun).apply(create)
fun MetaPermission(create: PermissionBuilder.() -> Unit) = PermissionBuilder(PERMISSION_Meta).apply(create)

class RoleBuilder : BlockBuilder(BLOCK_ROLE)

fun Role(create: RoleBuilder.() -> Unit) = RoleBuilder().apply(create)

class IdentityBuilder : BlockBuilder(BLOCK_IDENTITY)

fun Identity(create: IdentityBuilder.() -> Unit) = IdentityBuilder().apply(create)
//endregion