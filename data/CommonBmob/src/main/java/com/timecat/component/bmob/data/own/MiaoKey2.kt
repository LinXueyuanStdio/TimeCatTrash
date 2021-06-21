package com.timecat.component.bmob.data.own

import android.text.TextUtils
import androidx.annotation.IntDef
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobDate
import com.timecat.component.bmob.data._User
import com.timecat.identity.data.base.IStatus
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
data class MiaoKey2(
    var miaoKey: String,//key
    var generateBy: _User,
    var user: _User,
    @Cat var cat: Int = TimeCat,
    var right: Long = RIGHT_TimeCat,
    var startDate: BmobDate?,//第一次开始使用的时间
    var validity: Long = 2592000000L//有效期，单位毫秒：ms
) : BmobObject("MiaoKey"), IStatus {

    companion object {
        //下面是权限，开发喵在数据库生成 MiaoKey 的时候记得决定哪些权限可以用，可以用的设为 true
        const val Forever = 0x0001L//永久，可跳过validity有效期的判断逻辑
        const val AppEnhanceNotify = 0x0002L//增强通知
        const val NovelBackground = 0x0004L//小说自定义背景
        const val MasterTheme = 0x0008L//主题
        const val ProSource = 0x0010L//高级书源
        const val ProGithub = 0x0020L//Github Pro
        const val SyncTask = 0x0040L//同步[任务]
        const val SyncNote = 0x0080L//同步[笔记]
        const val SyncHabit = 0x0100L//同步[习惯]
        const val DataManager = 0x0200L//数据控制中心

        const val RIGHT_TimeCat: Long = 0//时光喵 不再需要，在MiaoKey表里找不到，说明是普通时光猫
        const val RIGHT_DeveloperCat: Long = Forever or AppEnhanceNotify or NovelBackground or
                MasterTheme or ProSource or ProGithub or SyncTask or SyncNote or SyncHabit or
                DataManager//开发喵
        const val RIGHT_CreatorCat: Long = AppEnhanceNotify or NovelBackground or
                MasterTheme or ProSource or ProGithub or SyncTask or SyncNote or SyncHabit or
                DataManager//造物喵

        @IntDef(DeveloperCat, TimeCat, CreatorCat, GenesisCat, ForeverCat, LoveCat)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Cat

        const val DeveloperCat = -1//开发喵
        const val TimeCat = 0//时光喵 不再需要，在MiaoKey表里找不到，说明是普通时光猫
        const val CreatorCat = 1//造物喵
        const val GenesisCat = 2//创世喵
        const val ForeverCat = 3//永恒喵
        const val LoveCat = 4//爱喵
        const val catNum = LoveCat + 1//喵的种类数
    }

    fun catName(): String = when (cat) {
        DeveloperCat -> "开发喵"
        CreatorCat -> "造物喵"
        GenesisCat -> "创世喵"
        ForeverCat -> "永恒喵"
        LoveCat -> "爱喵"
        else -> "时光喵"
    }

    /**
     * 是否在有效期内，永久的也返回 true
     */
    fun isAvaliable(): Boolean {
        if (isStatusEnabled(Forever)) {
            return true
        }
        if (startDate == null) {
            return false
        }
        if (TextUtils.isEmpty(startDate?.date)) {
            return false
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA)
        try {
            val date = sdf.parse(startDate?.date)
            return date.time + validity > Date().time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }

    fun isSuperCat(@Cat cat: Int): Boolean {
        //是否是下列超级权限中的任意一个
        return (cat == DeveloperCat
                || cat == ForeverCat
                || cat == LoveCat
                || cat == GenesisCat)
    }

    fun genRandomNum(): String {
        return genRandomNum(16)
    }

    /**
     * 生成随机密码
     *
     * @return 密码的字符串
     */
    fun genRandomNum(pwd_len: Int): String {
        var i: Int // 生成的随机数
        var count = 0 // 生成的密码的长度
        val str = charArrayOf(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        )
        val pwd = StringBuilder()
        val r = Random()
        while (count < pwd_len) {
            // 生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(str.size - 1)) // 生成的数最大为密码长度-1
            if (i >= 0 && i < str.size) {
                pwd.append(str[i])
                count++
            }
        }
        return pwd.toString()
    }

    fun nextCat() {
        cat = (cat + 1) % catNum
    }

    fun Forever(): Boolean = isStatusEnabled(Forever)
    fun AppEnhanceNotify(): Boolean = isStatusEnabled(AppEnhanceNotify)
    fun NovelBackground(): Boolean = isStatusEnabled(NovelBackground)
    fun MasterTheme(): Boolean = isStatusEnabled(MasterTheme)
    fun ProSource(): Boolean = isStatusEnabled(ProSource)
    fun ProGithub(): Boolean = isStatusEnabled(ProGithub)
    fun SyncTask(): Boolean = isStatusEnabled(SyncTask)
    fun SyncNote(): Boolean = isStatusEnabled(SyncNote)
    fun SyncHabit(): Boolean = isStatusEnabled(SyncHabit)
    fun DataManager(): Boolean = isStatusEnabled(DataManager)

    fun describe(): String {
        return catName() + ", " + describeStartDate() + "\n" +
                describeEndDate() + "\n" +
                describeForever() + "\n" +
                describeAppEnhanceNotify() + "\n" +
                describeNovelBackground() + "\n" +
                describeMasterTheme() + "\n" +
                describeProSource() + "\n" +
                describeProGithub() + "\n" +
                describeSyncTask() + "\n" +
                describeSyncNote() + "\n" +
                describeSyncHabit() + "\n" +
                describeDataManager()
    }

    private fun describeStartDate(): String = if (startDate == null) {
        "未使用"
    } else {
        "启用时间 " + startDate?.date
    }

    private fun describeEndDate(): String {
        if (cat == CreatorCat) {
            return yesOrNot(true) + " 30天"
        }
        return if (cat < 0 || cat > catNum) {//普通的时光猫
            yesOrNot(false) + " 永久"
        } else yesOrNot(true) + if (isStatusEnabled(Forever)) " 永久" else " 30天"
    }

    private fun describeForever(): String {
        return yesOrNot(Forever()) + " " + "永久"
    }

    private fun describeAppEnhanceNotify(): String {
        return yesOrNot(AppEnhanceNotify()) + " " + "增强通知"
    }

    private fun describeNovelBackground(): String {
        return yesOrNot(NovelBackground()) + " " + "小说自定义背景"
    }

    private fun describeMasterTheme(): String {
        return yesOrNot(MasterTheme()) + " " + "主题"
    }

    private fun describeProSource(): String {
        return yesOrNot(ProSource()) + " " + "高级书源"
    }

    private fun describeProGithub(): String {
        return yesOrNot(ProGithub()) + " " + "Github Pro"
    }

    private fun describeSyncTask(): String {
        return yesOrNot(SyncTask()) + " " + "同步[任务]"
    }

    private fun describeSyncNote(): String {
        return yesOrNot(SyncNote()) + " " + "同步[笔记]"
    }

    private fun describeSyncHabit(): String {
        return yesOrNot(SyncHabit()) + " " + "同步[习惯]"
    }

    private fun describeDataManager(): String {
        return yesOrNot(DataManager()) + " " + "数据控制中心"
    }

    private fun yesOrNot(b: Boolean): String {
        return if (b) "✓" else "✗"
    }

    //region Status 用 16 进制管理状态


    /**
     * 往状态集中加一个状态
     * @param status status
     */
    override fun addStatus(status: Long) {
        this.right = this.right or status
    }

    /**
     * 往状态集中移除一个状态
     * @param status status
     */
    override fun removeStatus(status: Long) {
        this.right = this.right and status.inv()
    }

    /**
     * 状态集中是否包含某状态
     * @param status status
     */
    override fun isStatusEnabled(status: Long): Boolean {
        return this.right and status != 0L
    }

    override fun statusDescription(): String {
        val stringBuilder = StringBuilder()
        return stringBuilder.toString()
    }
    //endregion
}
