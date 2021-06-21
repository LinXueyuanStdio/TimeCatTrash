package com.timecat.component.service;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-28
 * @description 图床服务
 * @usage null
 */
public interface PluginService {

    void start(InputToPlugin input, OutputFromPlugin output);

    boolean existPlugin(String path);

    interface InputToPlugin {
        String downloadUrl();

        String filename();

        boolean isPlugin();

        Bundle extra();

        @Nullable
        String action();

        @Nullable
        Uri uri();

        String partKey();

        String activityClassName();
    }

    interface OutputFromPlugin {
        void onDownloadFail(String s);

        void onDownloadSuccess();

        void setEnabled(boolean enabled);

        void onDownloadStart();

        void onDownloadProgress(int progress, int total);

        void onLoading(View view);

        void onCloseLoadingView();

        void onEnterFail(String s);

        void onEnterComplete();
    }

}
