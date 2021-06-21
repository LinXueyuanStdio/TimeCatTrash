package com.timecat.component.bmob.core;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.integration.ConfigModule;
import com.timecat.component.bmob.BuildConfig;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import timber.log.Timber;

/**
 * ================================================ CommonSDK 的 GlobalConfiguration
 * 含有有每个组件都可公用的配置信息, 每个组件的 AndroidManifest 都应该声明此 ConfigModule
 *
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#3.3">ConfigModule wiki 官方文档</a>
 * Created by JessYan on 30/03/2018 17:16
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GlobalConfiguration implements ConfigModule {

  @Override
  public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
  }

  @Override
  public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
    // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑

    lifecycles.add(0, new AppLifecycles() {

      @Override
      public void attachBaseContext(@NonNull Context base) {
      }

      @Override
      public void onCreate(@NonNull Application application) {
        //初始化Bmob IM
//                if (application.getApplicationInfo().packageName.equals(getMyProcessName())) {
//                    LogUtil.e("BmobIM init start");
//                    BmobIM.init(application);
//                    BmobIM.registerDefaultMessageHandler(new MessageHandler(application));
//                    LogUtil.e("BmobIM init end");
//                }
        //初始化Bmob
        Timber.d("Bmob init start");
        BmobConfig config = new BmobConfig.Builder(application)
            //设置appkey
            .setApplicationId(BuildConfig.BMOB_APP_ID)
            //请求超时时间（单位为秒）：默认15s
            .setConnectTimeout(10)
            //文件分片上传时每片的大小（单位字节），默认512*1024
            .setUploadBlockSize(1024 * 1024)
            //文件的过期时间(单位为秒)：默认1800s
            .setFileExpiration(1800)
            .build();
        Bmob.initialize(config);
        Timber.d("Bmob init end");
      }

      @Override
      public void onTerminate(@NonNull Application application) {

      }
    });
  }

  @Override
  public void injectActivityLifecycle(Context context,
      List<Application.ActivityLifecycleCallbacks> lifecycles) {
  }

  @Override
  public void injectFragmentLifecycle(Context context,
      List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
//        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
//
//            @Override
//            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
//                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
//                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
//                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
//                f.setRetainInstance(true);
//            }
//        });
  }

}
