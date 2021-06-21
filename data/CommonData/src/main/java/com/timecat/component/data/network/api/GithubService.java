package com.timecat.component.data.network.api;

import com.timecat.component.data.model.api.GitFile;
import com.timecat.component.data.model.api.GitFileResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
public interface GithubService {
    String GithubToken = "6e14fde960f1231468a5f41b767f01559d461b9c";
    String owner = "LinXueyuanStdio";
    String repo = "TimeCatOSS";
    String imagePathPrefix = "image/";
    String urlPathPrefix = "http://xichen.pub/TimeCatOSS";

    @Headers({"Content-UsageType: application/json", "Accept: application/json"})
    @PUT("/repos/{owner}/{repo}/contents/{path}")
    Observable<GitFileResponse> upload(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("path") String path,
            @Query("access_token") String access_token,
            @Body GitFile gitFile
    );
}
