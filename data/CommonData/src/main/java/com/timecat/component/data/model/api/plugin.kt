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
data class Plugin(
    val code: Int,
    val data: List<PluginSeries>
) : Serializable

data class PluginSeries(
    val name: String,
    val data: List<PluginInfo>
) : Serializable

data class PluginInfo(
    val plugin_type: Int,
    val plugin_version_code: Int,
    val plugin_filename: String,
    val plugin_download_url: String
) : Serializable

interface PluginDownloadService {
    @GET
    operator fun get(@Url url: String): Observable<Plugin>

    @Streaming //大文件时要加不然会OOM
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>
}