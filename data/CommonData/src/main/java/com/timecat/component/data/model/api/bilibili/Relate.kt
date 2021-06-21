package com.timecat.component.data.model.api.bilibili

data class Relate(
        val ad_index: Int,
        val aid: Int,
        val card_index: Int,
        val cid: Int,
        val client_ip: String,
        val duration: Int,
        val goto: String,
        val is_ad_loc: Boolean,
        val owner: Owner,
        val param: String,
        val pic: String,
        val request_id: String,
        val src_id: Int,
        val stat: Stat,
        val title: String,
        val uri: String
)