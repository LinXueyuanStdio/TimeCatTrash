package com.timecat.component.data.model.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-08-14
 * @description null
 * @usage null
 */
data class Skin(
    val code: Int,
    val data: List<SkinSeries>
) : Serializable

data class SkinSeries(
    val name: String,
    val data: List<SkinInfo>
) : Serializable

data class SkinInfo(
    val skin_name: String,
    val skin_url: String,
    val skin_filename: String,
    val skin_avatar: String,
    val skin_preview_0: String,
    val skin_preview_1: String,
    val skin_preview_2: String,
    val skin_intro: String,
    val skin_size: String
) : Serializable

interface SkinService {
    @GET
    operator fun get(@Url url: String): Observable<Skin>

    @Streaming //大文件时要加不然会OOM
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>
}