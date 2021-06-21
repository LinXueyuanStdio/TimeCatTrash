package com.timecat.component.service;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-28
 * @description 图床服务
 * @usage null
 */
public interface PictureBedService {
    void start(Context context, List<String> url, Work work);

    interface Work {
        /**
         * 加载插件
         *
         * @param view 自定义
         */
        void onShowLoadingView(View view);

        /**
         * 加载完毕
         */
        void onCloseLoadingView();

        /**
         * 加载完毕
         */
        void onEnterComplete();

        /**
         * 加载失败
         * @param msg 错误信息
         */
        void onEnterFail(String msg);
    }

}
