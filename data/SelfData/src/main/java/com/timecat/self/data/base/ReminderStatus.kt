package com.timecat.self.data.base

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-20
 * @description null
 * @usage null
 */
class ReminderStatus(var status: Int) {
    fun isUnderWay(): Boolean = isStatusEnable(UNDERWAY)

    fun isExpired(): Boolean = isStatusEnable(EXPIRED)

    fun isReminded(): Boolean = isStatusEnable(REMINDED)

    fun isStatusEnable(status: Int): Boolean {
        return this.status == status
    }

    companion object {
        @JvmStatic
        fun ofUnderWay(): ReminderStatus = ReminderStatus(UNDERWAY)

        @JvmStatic
        fun ofExpired(): ReminderStatus = ReminderStatus(EXPIRED)

        @JvmStatic
        fun ofReminded(): ReminderStatus = ReminderStatus(REMINDED)

        const val UNDERWAY = 0
        const val REMINDED = 2
        const val EXPIRED = 3
    }
}

