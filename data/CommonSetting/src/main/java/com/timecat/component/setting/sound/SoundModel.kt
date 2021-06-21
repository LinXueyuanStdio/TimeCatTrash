package com.timecat.component.setting.sound

import android.net.Uri

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/26
 * @description 音频的数据模型
 * @usage null
 */
class SoundModel(val name: String? = null, val uri: Uri? = null, val isSelected: Boolean = false) {
    override fun toString(): String {
        return uri?.toString() ?: name ?: ""
    }
}