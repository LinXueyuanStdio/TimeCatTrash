package com.timecat.component.data.network.api;

import com.timecat.component.data.model.api.ImageUpload;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


//Content-Disposition: form-data; name="smfile"; filename="1.png"
//Content-UsageType: image/png
public interface ImageUploadService {
    @Headers({"Accept:*/*", "Accept-Encoding:gzip, deflate", "Accept-Language:zh-CN,zh;q=0.8", "Connection:keep-alive", "Content-UsageType:multipart/form-data", "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36"})
    // @Multipart
    @POST("/api/upload")
    Observable<ImageUpload> uploadImage4search(@Body RequestBody imgs);
    // Observable<ImageUpload> uploadImage4search(@PartMap Map<String, Object> fields, @Body RequestBody imgs);
    //@Part("smfile; filename=\"abc.jpg\"")
}
