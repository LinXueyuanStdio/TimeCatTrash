package com.timecat.module.main.miniapp.activities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.timecat.component.commonsdk.utils.phone.NotificationChannelUtils
import com.timecat.component.readonly.RouterHub
import com.timecat.middle.setting.BaseSettingActivity
import com.timecat.module.main.R
import com.timecat.module.main.miniapp.notifications.*
import com.timecat.module.main.miniapp.utilities.GeneralUtils

@Route(path = RouterHub.MAIN_ChooseShortcutsActivity)
class ChooseShortcutsActivity : BaseSettingActivity() {
    data class MiniAppItem(
        @StringRes val strId: Int,
        @DrawableRes val drawableId: Int
    )

    lateinit var items: Array<MiniAppItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        items = arrayOf(
            MiniAppItem(R.string.main_miniapp_calculator, R.mipmap.calculator),
            MiniAppItem(R.string.main_miniapp_Notes, R.mipmap.notes),
            MiniAppItem(R.string.main_miniapp_Paint, R.mipmap.paint),
            MiniAppItem(R.string.main_miniapp_Music, R.mipmap.player),
            MiniAppItem(R.string.main_miniapp_browser, R.mipmap.browser),
            MiniAppItem(R.string.main_miniapp_camera, R.mipmap.camera),
            MiniAppItem(R.string.main_miniapp_dialer, R.mipmap.dialer),
            MiniAppItem(R.string.main_miniapp_contacts, R.mipmap.contacts),
            MiniAppItem(R.string.main_miniapp_Recorder, R.mipmap.recorder),
            MiniAppItem(R.string.main_miniapp_calender, R.mipmap.calender),
            MiniAppItem(R.string.main_miniapp_Volume, R.mipmap.volume),
            MiniAppItem(R.string.main_miniapp_files, R.mipmap.files),
            MiniAppItem(R.string.main_miniapp_Stopwatch, R.mipmap.stopwatch),
            MiniAppItem(R.string.main_miniapp_flashlight, R.mipmap.flashlight),
            MiniAppItem(R.string.main_miniapp_Video, R.mipmap.video),
            MiniAppItem(R.string.main_miniapp_gallery, R.mipmap.gallery),
            MiniAppItem(R.string.main_miniapp_Launcher, R.mipmap.launcher),
            MiniAppItem(R.string.main_miniapp_System, R.mipmap.system),
            MiniAppItem(R.string.main_miniapp_facebook, R.mipmap.facebook),
            MiniAppItem(R.string.main_miniapp_Maps, R.mipmap.maps),
            MiniAppItem(R.string.main_miniapp_GMail, R.mipmap.gmail),
            MiniAppItem(R.string.main_miniapp_Twitter, R.mipmap.twitter),
            MiniAppItem(R.string.main_miniapp_YouTube, R.mipmap.youtube),
            MiniAppItem(R.string.main_miniapp_bilibili, R.drawable.ic_bilibili_pink_24dp)
        )
        super.onCreate(savedInstanceState)
    }

    override fun addSettingItems(container: ViewGroup) {
        for (i in 0..5) {
            simpleNotify(container, i)
        }
    }

    private fun simpleNotify(container: ViewGroup, i: Int) {
        val item = items.firstOrNull {
            it.drawableId == GeneralUtils.GetNotificationIcon(this, i)
        } ?: items[0]
        simpleNext(container, "快捷方式$i", null, getString(item.strId), null) {
            AlertDialog.Builder(this)
                .setSingleChoiceItems(
                    items.map { getString(it.strId) }.toTypedArray(),
                    items.indexOf(item)
                ) { dialog, _ ->
                    dialog.dismiss()
                    val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    it.setText(getString(items[selectedPosition].strId))
                    GeneralUtils.SetNotificationIcon(this, i, items[selectedPosition].drawableId)
                    stopNotification()
                    startNotification()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun title(): String = "零应用快捷方式"

    private fun startNotification() {
        NotificationChannelUtils.createNotificationChannel(this, SHORTCUTS_CHANNEL_ID)
        val notificationManager = NotificationChannelUtils.getNotificationManager(this)
        val notification = NotificationCompat.Builder(this, SHORTCUTS_CHANNEL_ID)
            .setChannelId(SHORTCUTS_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setTicker(null)
            .setWhen(System.currentTimeMillis())
            .build()
        val notificationView = RemoteViews(packageName, R.layout.notification_miniapps_white_trans)
        notificationView.setImageViewResource(
            R.id.imageViewNotificationButton1,
            GeneralUtils.GetNotificationIcon(context, 0)
        )
        notificationView.setImageViewResource(
            R.id.imageViewNotificationButton2,
            GeneralUtils.GetNotificationIcon(context, 1)
        )
        notificationView.setImageViewResource(
            R.id.imageViewNotificationButton3,
            GeneralUtils.GetNotificationIcon(context, 2)
        )
        notificationView.setImageViewResource(
            R.id.imageViewNotificationButton4,
            GeneralUtils.GetNotificationIcon(context, 3)
        )
        notificationView.setImageViewResource(
            R.id.imageViewNotificationButton5,
            GeneralUtils.GetNotificationIcon(context, 4)
        )
        notificationView.setImageViewResource(
            R.id.imageViewNotificationButton6,
            GeneralUtils.GetNotificationIcon(context, 5)
        )
        val pendingNotificationIntent =
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MiniAppsActivity::class.java),
                0
            )
        notification.contentView = notificationView
        notification.contentIntent = pendingNotificationIntent
        notification.flags = notification.flags or 32
        val pendingIntent1 = PendingIntent.getActivity(
            this,
            0,
            Intent(this, Shortcut1Activity::class.java),
            0
        )
        val pendingIntent2 = PendingIntent.getActivity(
            this,
            0,
            Intent(this, Shortcut2Activity::class.java),
            0
        )
        val pendingIntent3 = PendingIntent.getActivity(
            this,
            0,
            Intent(this, Shortcut3Activity::class.java),
            0
        )
        val pendingIntent4 = PendingIntent.getActivity(
            this,
            0,
            Intent(this, Shortcut4Activity::class.java),
            0
        )
        val pendingIntent5 = PendingIntent.getActivity(
            this,
            0,
            Intent(this, Shortcut5Activity::class.java),
            0
        )
        val pendingIntent6 = PendingIntent.getActivity(
            this,
            0,
            Intent(this, Shortcut6Activity::class.java),
            0
        )
        notificationView.setOnClickPendingIntent(
            R.id.imageViewNotificationButton1,
            pendingIntent1
        )
        notificationView.setOnClickPendingIntent(
            R.id.imageViewNotificationButton2,
            pendingIntent2
        )
        notificationView.setOnClickPendingIntent(
            R.id.imageViewNotificationButton3,
            pendingIntent3
        )
        notificationView.setOnClickPendingIntent(
            R.id.imageViewNotificationButton4,
            pendingIntent4
        )
        notificationView.setOnClickPendingIntent(
            R.id.imageViewNotificationButton5,
            pendingIntent5
        )
        notificationView.setOnClickPendingIntent(
            R.id.imageViewNotificationButton6,
            pendingIntent6
        )
        notificationView.setOnClickPendingIntent(
            R.id.imageViewNotificationButtonMore,
            pendingNotificationIntent
        )
        notificationManager.notify(100, notification)
    }

    private fun stopNotification() {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(100)
    }

    companion object {
        const val SHORTCUTS_CHANNEL_ID = "shortcuts_channel_id"
    }
}