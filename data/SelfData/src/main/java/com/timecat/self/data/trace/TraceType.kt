package com.timecat.self.data.trace

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
@IntDef(
    TRACE_ACTION_CLICK,
    TRACE_ACTION_LIKE,
    TRACE_ACTION_DING,
    TRACE_ACTION_SCORE,
    TRACE_ACTION_DOWNLOAD,
    TRACE_ACTION_FOCUS,
    TRACE_ACTION_VOTE,
    TRACE_ACTION_RECOMMEND,
)
@Retention(AnnotationRetention.SOURCE)
annotation class TraceType

const val TRACE_ACTION_CLICK: Int = 0 // 点击
const val TRACE_ACTION_LIKE: Int = 1 // 点赞
const val TRACE_ACTION_DING: Int = 2 // 拍了拍
const val TRACE_ACTION_SCORE: Int = 3 // 评分
const val TRACE_ACTION_DOWNLOAD: Int = 4 // 下载
const val TRACE_ACTION_FOCUS: Int = 5 // 关注
const val TRACE_ACTION_VOTE: Int = 6 // 投票
const val TRACE_ACTION_RECOMMEND: Int = 7 // 推荐
