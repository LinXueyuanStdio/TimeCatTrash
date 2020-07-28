package com.timecat.module.main.miniapp.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.alibaba.android.arouter.facade.annotation.Route
import com.timecat.component.readonly.Constants
import com.timecat.component.readonly.RouterHub
import com.timecat.component.setting.DEF
import com.timecat.middle.setting.BaseSettingActivity
import com.timecat.module.main.R
import com.timecat.module.main.miniapp.notifications.MiniAppNotification
import com.timecat.module.main.miniapp.utilities.SettingsUtils

@Route(path = RouterHub.MAIN_SettingMiniAppActivity)
class SettingMiniAppActivity : BaseSettingActivity() {

    override fun title(): String {
        return "零应用设置"
    }

    override fun addSettingItems(container: ViewGroup) {
        simpleSwitch(container, getString(R.string.show_quick_launch), {
            DEF.config().getBoolean(Constants.IS_SHOW_APP_SET_NOTIFY, false)
        }) {
            DEF.config().save(Constants.IS_SHOW_APP_SET_NOTIFY, it)
            if (it) {
                MiniAppNotification.startNotification(this@SettingMiniAppActivity)
            } else {
                MiniAppNotification.stopNotification(this@SettingMiniAppActivity)
            }
        }

        simpleNext(
            container,
            getString(R.string.choose_launcher_shortcuts),
            RouterHub.MAIN_ChooseShortcutsActivity
        )

        val value = SettingsUtils.GetValue(this, "WINDOW_OPACITY")
        var opacity = if (value == "") 0.9 else value.toDouble()
        simpleNext(
            container,
            getString(R.string.inactive_window_opacity),
            null,
            "${(opacity * 10.0).toInt()}0%",
            null
        ) {
            val layout = LayoutInflater.from(this).inflate(R.layout.seekbar_dialog, null)
            val builder = AlertDialog.Builder(context).setView(layout)
            builder.setTitle("Opacity")
            val tvProgress = layout.findViewById<TextView>(R.id.textViewSeekbarProgress)
            val seek = layout.findViewById<SeekBar>(R.id.seekBar)
            seek.max = 5
            seek.progress = (opacity * 10.0).toInt() - 5
            seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    tvProgress.text = "${(progress + 5)}0%"
                }
            })
            tvProgress.text = "${(seek.progress + 5)}0%"
            builder.setPositiveButton("OK") { _, _ ->
                it.setText("${(seek.progress + 5)}0%")
                opacity = (seek.progress + 5).toDouble() / 10.0
                SettingsUtils.SetValue(this, "WINDOW_OPACITY", "$opacity")
            }
            builder.setNegativeButton("CANCEL") { dialog, _ -> dialog.cancel() }
            builder.create().show()
        }
    }
}