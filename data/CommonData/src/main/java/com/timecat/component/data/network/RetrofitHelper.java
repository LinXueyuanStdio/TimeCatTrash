package com.timecat.component.data.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timecat.extend.arms.BaseApplication;
import com.timecat.component.data.model.api.PluginDownloadService;
import com.timecat.component.data.model.api.SkinService;
import com.timecat.component.data.network.api.BilibiliService;
import com.timecat.component.data.network.api.GiteeService;
import com.timecat.component.data.network.api.GithubService;
import com.timecat.component.data.network.api.ImageUploadService;
import com.timecat.component.data.network.api.OcrService;
import com.timecat.component.data.network.api.PicUploadService;
import com.timecat.component.data.network.api.TranslationService;
import com.timecat.component.data.network.api.WordSegmentService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.timecat.component.data.network.ConstantURL.IMAGE_UPLOAD_URL;
import static com.timecat.component.data.network.ConstantURL.OCR_URL;
import static com.timecat.component.data.network.ConstantURL.PIC_UPLOAD_URL;
import static com.timecat.component.data.network.ConstantURL.SEGMENT_URL;
import static com.timecat.component.data.network.ConstantURL.YOUDAO_URL;

public class RetrofitHelper {

    private static Gson gson = new GsonBuilder().setLenient().create();
    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }

    /**
     * 初始化OKHttpClient 设置缓存 设置超时时间 设置打印日志 设置UA拦截器
     */
    private static void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLog());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    //设置Http缓存
                    Cache cache = new Cache(new File(BaseApplication.getContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    public static TranslationService getTranslationService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YOUDAO_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(TranslationService.class);
    }

    public static WordSegmentService getWordSegmentService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SEGMENT_URL).client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WordSegmentService.class);
    }

    public static OcrService getOcrService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OCR_URL).client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(OcrService.class);
    }

    public static ImageUploadService getImageUploadService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IMAGE_UPLOAD_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ImageUploadService.class);
    }

    public static PicUploadService getPicUploadService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PIC_UPLOAD_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(PicUploadService.class);
    }

    public static BilibiliService getBilibiliService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantURL.BASE_BILIBILI_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(BilibiliService.class);
    }
    public static GithubService getGithubService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GithubService.class);
    }
    public static GiteeService getGiteeService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gitee.com/")
                .client(new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                LogUtils.e(chain.request().toString());
                                return chain.proceed(chain.request());
                            }
                        })
                        .build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GiteeService.class);
    }
    public static SkinService getSkinService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gitee.com/LinXueyuanCosmos/TimeCatSkin/raw/master/")
                .client(new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(SkinService.class);
    }

    public static PluginDownloadService getPluginService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gitee.com/LinXueyuanCosmos/TimeCatPlugin/raw/master/")
                .client(new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .cache(null)
                        .build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(PluginDownloadService.class);
    }

    public static class HttpLog implements HttpLoggingInterceptor.Logger {

        @Override
        public void log(@NonNull String message) {
            Log.d("RetrofitHttpLog", message);
        }
    }

    /**
     * 添加UA拦截器 B站请求API文档需要加上UA
     */
    private static class UserAgentInterceptor implements Interceptor {

        private static final String COMMON_UA_STR = "";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request requestWithUserAgent = originalRequest.newBuilder().removeHeader("User-Agent")
                    .addHeader("User-Agent", COMMON_UA_STR).build();
            return chain.proceed(requestWithUserAgent);
        }
    }
}
