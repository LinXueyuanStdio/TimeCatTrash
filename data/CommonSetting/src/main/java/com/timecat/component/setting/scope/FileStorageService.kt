package com.timecat.component.setting.scope

import com.timecat.component.setting.DIR

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/1
 * @description 文件存储服务
 * @usage null
 */
interface FileStorageService {
    fun get(url: String, dir: DIR, filename: String)
}