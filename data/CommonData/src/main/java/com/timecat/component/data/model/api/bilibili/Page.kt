package com.timecat.component.data.model.api.bilibili

data class Page(
        val cid: Int,
        val dimension: Dimension,
        val dm: Dm,
        val dmlink: String,
        val duration: Int,
        val from: String,
        val metas: List<Meta>,
        val page: Int,
        val part: String,
        val vid: String,
        val weblink: String
)