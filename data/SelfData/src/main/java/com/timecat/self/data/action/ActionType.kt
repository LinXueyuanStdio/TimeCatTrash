package com.timecat.self.data.action

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description null
 * @usage null
 */
//投票、点赞、打卡、评分
@IntDef(
    ACTION_CLICK,
    ACTION_LIKE,
    ACTION_DING,
    ACTION_SCORE,
    ACTION_DOWNLOAD,
    ACTION_FOCUS,
    ACTION_VOTE,
    ACTION_RECOMMEND,
)
@Retention(AnnotationRetention.SOURCE)
annotation class ActionType

const val ACTION_CLICK: Int = 0 // 点击
const val ACTION_LIKE: Int = 1 // 点赞
const val ACTION_DING: Int = 2 // 拍了拍
const val ACTION_SCORE: Int = 3 // 评分
const val ACTION_DOWNLOAD: Int = 4 // 下载
const val ACTION_FOCUS: Int = 5 // 关注
const val ACTION_VOTE: Int = 6 // 投票
const val ACTION_RECOMMEND: Int = 7 // 推荐
