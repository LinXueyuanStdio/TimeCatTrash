package com.timecat.component.data.network.api;

import com.timecat.component.data.model.api.GitFileResponse;
import com.timecat.component.data.model.api.GiteeFile;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
public interface GiteeService {
    String GiteeToken = "fa2895341b57ecbd9e9a468b50df72a5";
    String owner = "LinXueyuanCosmos";
    String repo = "TimeCatOSS";
    String imagePathPrefix = "image/";
    String urlPathPrefix = "https://gitee.com/" + owner + "/" + repo + "/raw/master/";

    //https://gitee.com/LinXueyuanCosmos/TimeCatOSS/raw/master/icon/test.png

    @Headers({"Content-UsageType: application/json", "Accept: application/json"})
    @POST("/api/v5/repos/{owner}/{repo}/contents/{path}")
    Observable<GitFileResponse> upload(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("path") String path,
//            @Query("access_token") String access_token,
            @Body GiteeFile gitFile
    );
}
