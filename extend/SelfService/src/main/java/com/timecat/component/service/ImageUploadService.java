package com.timecat.component.service;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/12
 * @description null
 * @usage null
 */
public interface ImageUploadService {
    void upload(@NonNull Activity activity, @NonNull String path, @Nullable UploadCallback uploadCallback);

    interface UploadCallback {
        void onSuccess(@NonNull String url);

        void onFail(@NonNull String e);
    }
}