/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.timecat.component.theme;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.jess.arms.base.delegate.AppLifecycles;
import com.timecat.component.theme.skin.CustomSDCardLoader;

import skin.support.SkinCompatManager;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        SkinCompatManager.withoutActivity(application)
                         // 自定义加载策略，指定SDCard路径
                         .addStrategy(new CustomSDCardLoader())
                //Material Component 组件已经够用，样式好看
                //拦截后的样式不好看，所以去掉拦截器 Inflater
//                         .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
//                         .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
//                         .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
//                         .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                         .setSkinWindowBackgroundEnable(true)                   // 关闭windowBackground换肤，默认打开[可选]
                         .setSkinAllActivityEnable(false) // 不是所有的activity都要，比如插件里的 Activity
                         .loadSkin();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
