package com.timecat.component.data.network.api;


import com.timecat.component.data.BuildConfig;
import com.timecat.component.data.model.api.BiliBiliVideo;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/2/20
 * @description null
 * @usage null
 */
public interface BilibiliService {

    @Headers({"Content-UsageType: application/json", "Accept: application/json"})
    @GET("http://app.bilibili.com/x/view?_device=wp&appkey%20=" +
            BuildConfig.BILIBILI_APPKEY +
            "&build=411005&plat=4&platform=android&ts=1517542738232&_ulv=10000")
    Observable<BiliBiliVideo> getBiliCoverByAV(@Query("aid") String av_id);
}
