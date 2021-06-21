package com.timecat.component.service;

import android.app.Application;
import android.content.Context;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-27
 * @description 腾讯统计SDK
 * @usage null
 */
public interface MtaService {
    void registerActivityLifecycleCallbacks(Application context);
    void startStatService(Context context);

    void setDebugEnable(boolean debugEnable);
}
