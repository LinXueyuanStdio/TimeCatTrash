package com.timecat.component.data.network.api;

import com.timecat.component.data.model.api.OcrItem;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by wangyan-pd on 2016/11/2.
 * <p>
 * e02e6b613488957
 */

public interface OcrService {
    @Headers({"User-Agent: Mozilla/5.0", "accept-language:zh-CN,zh;q=0.8"})
    @Multipart// 参数的类型都应该是RequestBody，不然上传的图片的时候会报JSON must start with an array or an object错误
    @POST("parse/image/")
    Observable<OcrItem> uploadImage(@Part("apikey") String key, @Part("fileName") String description, @Part("file\"; filename=\"image.png\"") RequestBody imgs);
}
