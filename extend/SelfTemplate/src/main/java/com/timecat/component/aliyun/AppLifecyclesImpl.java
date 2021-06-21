package com.timecat.component.font;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alipay.mobile.framework.quinoxless.IInitCallback;
import com.alipay.mobile.framework.quinoxless.QuinoxlessFramework;
import com.alipay.mobile.nebula.provider.H5AppCenterPresetProvider;
import com.alipay.mobile.nebula.util.H5Utils;
import com.jess.arms.base.delegate.AppLifecycles;
import com.mpaas.nebula.adapter.api.MPNebula;
import com.mpaas.tinyappcommonres.TinyAppCenterPresetProvider;

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
        QuinoxlessFramework.setup(application, new IInitCallback() {
            @Override
            public void onPostInit() {
                // 预置小程序离线包
                H5Utils.setProvider(H5AppCenterPresetProvider.class.getName(), new TinyAppCenterPresetProvider());
//                // 设置小程序离线包验签公钥，如需关闭验签，可在 custom_config.json 中，将 h5_shouldverifyapp 的 value 设置成 NO
                MPNebula.enableAppVerification("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqG8T9E0K2cctGMs3qyBv" +
                        "b7rjBeNWMICRqlan6YgLg3YrI2ttV6D2AsTp7nkVm7o8Abbl5K1KyTXFTkoxhSBp" +
                        "M/wsDhiXGGKU1Yrr3RiT8dTea5ddgBPz0lC3kakv6gMnyBK5KnS8QC91NnmPR3BV" +
                        "/gWAXssdNWTMFzi2n8XbPNTI0AC8BbBH5T3xQ1LKi6KCHb7is/mMTG81ZnIoN3Dh" +
                        "FSmhal5exiW0b73QcIlGjtH2SQm3aq+fqzjlpbQbUtdJG8mhmNBcEFimouwfI6OF" +
                        "q0xVS/RSXstaAIXoEGiczzwisQoCa3JLFEzmBqH0CRSHq8Jjk75cM8lt6bj4oWTS" +
                        "ZQIDAQAB");
            }
        });
        QuinoxlessFramework.init();
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
