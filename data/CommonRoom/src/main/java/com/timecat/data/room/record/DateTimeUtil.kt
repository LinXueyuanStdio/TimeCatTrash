package com.timecat.data.room.record

import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Months
import org.joda.time.Weeks
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/26
 * @description null
 * @usage null
 */
object DateTimeUtil {

    fun calculateTimeGap(start: Long, end: Long, type: Int): Int {
        var sDt = DateTime(start).withTime(0, 0, 0, 0)
        var eDt = DateTime(end).withTime(0, 0, 0, 0)
        if (type == Calendar.DATE) {
            return Days.daysBetween(sDt, eDt).days
        } else if (type == Calendar.WEEK_OF_YEAR) {
            sDt = sDt.withDayOfWeek(1)
            eDt = eDt.withDayOfWeek(1)
            return Weeks.weeksBetween(sDt, eDt).weeks
        } else if (type == Calendar.MONTH) {
            sDt = sDt.withDayOfMonth(1)
            eDt = eDt.withDayOfMonth(1)
            return Months.monthsBetween(sDt, eDt).months
        } else if (type == Calendar.YEAR) {
            return eDt.year - sDt.year
        }
        return 0
    }

    fun getHabitReminderTime(type: Int, curHrTime: Long, vary: Int): Long {
        if (type == Calendar.DATE) {
            return curHrTime + vary * 86400000L
        } else if (type == Calendar.WEEK_OF_YEAR) {
            return curHrTime + vary * 604800000L
        }
        var dt = DateTime(curHrTime)
        var year = dt.year
        var month = dt.monthOfYear
        val day = dt.dayOfMonth
        if (type == Calendar.MONTH) {
            val days: Int = getDaysOfMonth(year, month)
            dt = dt.plusMonths(vary)
            if (day == days) {
                year = dt.year
                month = dt.monthOfYear
                dt = dt.withDayOfMonth(getDaysOfMonth(year, month))
            }
            return dt.millis
        } else if (type == Calendar.YEAR) {
            val days: Int = getDaysOfMonth(year, month)
            dt = dt.plusYears(vary)
            if (day == days) {
                dt = dt.withDayOfMonth(getDaysOfMonth(year + vary, month))
            }
            return dt.millis
        }
        return 0
    }

    fun getDaysOfMonth(y: Int, m: Int): Int {
        val days = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        if (y % 4 == 0 && (y % 100 != 0 || y % 400 == 0)) {
            days[1] = 29
        }
        return days[m - 1]
    }
}