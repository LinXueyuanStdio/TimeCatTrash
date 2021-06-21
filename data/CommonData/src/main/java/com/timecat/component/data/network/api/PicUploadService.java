package com.timecat.component.data.network.api;

import com.timecat.component.data.model.api.ImageUpload;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * https://yotuku.cn/api/upload/auto
 */
public interface PicUploadService {
    @Headers({"Accept:*/*", "Accept-Encoding:gzip, deflate", "Accept-Language:zh-CN,zh;q=0.8", "Connection:keep-alive", "Content-UsageType:text/plain", "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36", "X-Requested-With:XMLHttpRequest"})
    @POST("api/upload/auto?")
    Observable<ImageUpload> uploadImage4search(@Query("name") String name, @Query("type") String type, @Body String imgs);
}
