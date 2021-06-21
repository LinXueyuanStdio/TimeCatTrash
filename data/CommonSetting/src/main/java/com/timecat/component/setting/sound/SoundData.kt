package com.timecat.component.setting.sound

import android.net.Uri

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/27
 * @description null
 * @usage null
 */
class SoundData(var name: String, var url: String) {
    constructor() : this("", "")
    constructor(s: SoundModel) : this(s.name ?: "", s.uri?.toString() ?: "")

    fun toSoundModel(): SoundModel {
        return SoundModel(name, Uri.parse(url))
    }
}