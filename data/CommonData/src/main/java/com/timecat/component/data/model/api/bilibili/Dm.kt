package com.timecat.component.data.model.api.bilibili

data class Dm(
        val closed: Boolean,
        val count: Int,
        val mask: Mask,
        val real_name: Boolean
)