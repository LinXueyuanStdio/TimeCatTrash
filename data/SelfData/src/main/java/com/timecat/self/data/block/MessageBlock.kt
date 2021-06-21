package com.timecat.self.data.block

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description null
 * @usage null
 */
//region 高级结构：消息、私信
data class MessageBlock(val message: String)


@IntDef(
    BLOCK_FILE,
    BLOCK_WEB,
    BLOCK_TEXT,
    BLOCK_IMAGE,
    BLOCK_VIDEO,
    BLOCK_LOCATION,
    BLOCK_VOICE,
    BLOCK_CMD,
    BLOCK_CALL,
    BLOCK_BIG_EMOTION
)
@Retention(AnnotationRetention.SOURCE)
annotation class MessageType


const val BLOCK_FILE: Int = 0 // 文件 url
const val BLOCK_WEB: Int = 1 // 网页 url
const val BLOCK_TEXT: Int = 2 // 简单文本块
const val BLOCK_IMAGE: Int = 3 // 图片文件
const val BLOCK_VIDEO: Int = 4 // 视频文件
const val BLOCK_LOCATION: Int = 5 // 地理位置
const val BLOCK_VOICE: Int = 6 // 语音文件
const val BLOCK_CMD: Int = 7 // 控制型消息
const val BLOCK_CALL: Int = 8 // 打电话
const val BLOCK_BIG_EMOTION: Int = 9 // 表情包
//endregion