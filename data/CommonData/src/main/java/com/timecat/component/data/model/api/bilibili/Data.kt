package com.timecat.component.data.model.api.bilibili

data class Data(
        val aid: Int,
        val attribute: Int,
        val cid: Int,
        val cms: List<Cm>,
        val contributions: List<Contribution>,
        val copyright: Int,
        val ctime: Int,
        val desc: String,
        val dimension: Dimension,
        val dislike_reasons: List<DislikeReason>,
        val dm_seg: Int,
        val duration: Int,
        val dynamic: String,
        val owner: Owner,
        val owner_ext: OwnerExt,
        val pages: List<Page>,
        val pic: String,
        val pubdate: Int,
        val relates: List<Relate>,
        val req_user: ReqUser,
        val rights: Rights,
        val stat: Stat,
        val state: Int,
        val tag: List<Tag>,
        val tid: Int,
        val title: String,
        val tname: String,
        val videos: Int
)