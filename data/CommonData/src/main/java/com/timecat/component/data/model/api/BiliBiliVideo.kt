package com.timecat.component.data.model.api

import com.timecat.component.data.model.api.bilibili.Data

data class BiliBiliVideo(
        val code: Int,
        val data: Data,
        val message: String,
        val ttl: Int
)