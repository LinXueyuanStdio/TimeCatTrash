package com.timecat.self.data.action

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description null
 * @usage null
 */
// 投票
data class Vote(val weight: Short)

// 打卡
data class Ding(val timestamp: Long) // 对 timestamp 的打卡

// 点赞
data class Like(val weight: Short)

// 评分
data class Scores(val weight: Short)

// 时间块记录，name 来自 TagSection，timestamp 来自 View
data class TimeBlock(val timestamp: Long, val name: String)

// 下载
data class Download(val timestamp: Long)

data class Repetition(val timestamp: Long) // 原始打卡记录
data class CheckMark(val timestamp: Long, val value: Int)// 可以由 Repetition 计算出来
data class Score(val timestamp: Long, val score: Int) // 可以由 CheckMark 计算出来
data class Streak(val timestamp: Long) // 可以由 CheckMark 计算出来