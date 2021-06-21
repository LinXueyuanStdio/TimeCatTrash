package com.timecat.self.data.block.type

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description null
 * @usage null
 */
@IntDef(
    BLOCK_RECORD,
    BLOCK_DATABASE,
    BLOCK_NOVEL,
    BLOCK_ACCOUNT,
    BLOCK_MESSAGE,

    BLOCK_ABOUT,

    BLOCK_TAG,
    BLOCK_TOPIC,
    BLOCK_MEDIA,
    BLOCK_LEADER_BOARD,

    BLOCK_APP,
    BLOCK_COMMENT,
    BLOCK_RECOMMEND,
    BLOCK_CONVERSATION,
    BLOCK_CONTAINER,
    BLOCK_DIVIDER,

    BLOCK_FOCUS,
    BLOCK_PATH,
    BLOCK_CODE,
    BLOCK_DIALOG,
    BLOCK_PLUGIN,
    BLOCK_BUTTON,
    BLOCK_FORUM,
    BLOCK_POST,
    BLOCK_PERMISSION
)
@Retention(AnnotationRetention.SOURCE)
annotation class BlockType

const val BLOCK_RECORD: Int = 0 // 记录，当任务、笔记用（兼容了任务、笔记、计划、子计划、提醒等具有生命周期等）
const val BLOCK_DATABASE: Int = 1 // 数据库，表头属性名和个数可自定义，具体类型有计划、子计划、看板、表格
const val BLOCK_NOVEL: Int = 2 // 小说
const val BLOCK_ACCOUNT: Int = 3 // 账户，记录用户名和密码
const val BLOCK_MESSAGE: Int = 4 // 聊天消息

const val BLOCK_ABOUT: Int = 5 // 公告<<<<云端>>>>

const val BLOCK_TAG: Int = 6 // 标签<<<<云端>>>>
const val BLOCK_TOPIC: Int = 7 // 主题<<<<云端>>>>
const val BLOCK_MEDIA: Int = 8 // 媒体文件
const val BLOCK_LEADER_BOARD: Int = 10 // 排行榜<<<<云端>>>>

const val BLOCK_APP: Int = 11 // 排行榜里的APP<<<<云端>>>>
const val BLOCK_COMMENT: Int = 12 // 评论<<<<云端>>>>
const val BLOCK_RECOMMEND: Int = 13 // 推荐<<<<云端>>>>
const val BLOCK_CONVERSATION: Int = 14 // 本地聊天通道

const val BLOCK_CONTAINER: Int = 15 // 文件夹 : 收藏集、清单、笔记本、文件夹、列表
const val BLOCK_DIVIDER: Int = 16 // 分隔符

const val BLOCK_FOCUS: Int = 17 // 专注，有番茄钟专注或种树专注等等子类型
const val BLOCK_PATH: Int = 18 // 文件夹路径(本地文件夹、云端文件夹等)
const val BLOCK_CODE: Int = 19 // 脚本自动化

const val BLOCK_MOMENT: Int = 20 // 动态<<<<云端>>>>
const val BLOCK_DIALOG: Int = 21 // 即时通讯<<<<云端>>>>
const val BLOCK_PLUGIN: Int = 22 // 插件

const val BLOCK_LINK: Int = 23 // 间接符文，代表某个符文
const val BLOCK_BUTTON: Int = 24 // 执行按钮：按模板复制、打开、改变颜色等可编程的按钮
const val BLOCK_FORUM: Int = 25 // 论坛<<<<云端>>>>
const val BLOCK_POST: Int = 26 // 帖子<<<<云端>>>>
const val BLOCK_PERMISSION: Int = 27 // 权限<<<<云端>>>>
const val BLOCK_IDENTITY: Int = 28 // 身份<<<<云端>>>>
const val BLOCK_ROLE: Int = 29 // 角色<<<<云端>>>>

//region
const val BLOCK_NEXT: Int = 30 // 类型总数
//endregion
