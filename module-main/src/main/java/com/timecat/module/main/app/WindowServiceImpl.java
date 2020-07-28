package com.timecat.module.main.app;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.data.service.WindowService;
import com.timecat.component.readonly.RouterHub;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.apps.BiliBiliApp;
import com.timecat.module.main.miniapp.apps.BrowserApp;
import com.timecat.module.main.miniapp.apps.CalculatorApp;
import com.timecat.module.main.miniapp.apps.CalenderApp;
import com.timecat.module.main.miniapp.apps.CameraApp;
import com.timecat.module.main.miniapp.apps.ContactsApp;
import com.timecat.module.main.miniapp.apps.DialerApp;
import com.timecat.module.main.miniapp.apps.FacebookApp;
import com.timecat.module.main.miniapp.apps.FilesApp;
import com.timecat.module.main.miniapp.apps.FlashlightApp;
import com.timecat.module.main.miniapp.apps.GalleryApp;
import com.timecat.module.main.miniapp.apps.GmailApp;
import com.timecat.module.main.miniapp.apps.LauncherApp;
import com.timecat.module.main.miniapp.apps.MapsApp;
import com.timecat.module.main.miniapp.apps.MusicApp;
import com.timecat.module.main.miniapp.apps.NotesApp;
import com.timecat.module.main.miniapp.apps.PaintApp;
import com.timecat.module.main.miniapp.apps.RecorderApp;
import com.timecat.module.main.miniapp.apps.StopwatchApp;
import com.timecat.module.main.miniapp.apps.SystemApp;
import com.timecat.module.main.miniapp.apps.TwitterApp;
import com.timecat.module.main.miniapp.apps.VideoApp;
import com.timecat.module.main.miniapp.apps.VolumeApp;
import com.timecat.module.main.miniapp.apps.YoutubeApp;
import com.timecat.plugin.window.WindowAgreement;

import java.util.Random;

import io.reactivex.functions.Consumer;

/**
 * ================================================
 * 向外提供服务的接口实现类, 在此类中实现一些具有特定功能的方法提供给外部, 即可让一个组件与其他组件或宿主进行交互
 *
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#2.2.3.2">CommonService wiki 官方文档</a>
 * Created by JessYan on 2018/4/27 14:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Route(path = RouterHub.GLOBAL_WindowServiceImpl, name = "窗口服务")
public class WindowServiceImpl implements WindowService {
    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    @Override
    public void showCalculatorApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, CalculatorApp.class, CalculatorApp.id);
    }

    @Override
    public void showNotesApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, NotesApp.class, NotesApp.id);
    }

    @Override
    public void showPaintApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, PaintApp.class, PaintApp.id);
    }

    @Override
    public void showMusicApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
                    WindowAgreement.show(mContext, MusicApp.class, -100);
                }, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showBrowserApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, BrowserApp.class, BrowserApp.id);
    }

    @Override
    public void showCameraApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
                    WindowAgreement.show(mContext, CameraApp.class, -1000);
                }, Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showDialerApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
            WindowAgreement.show(mContext, DialerApp.class, DialerApp.id);
        }, Manifest.permission.CALL_PHONE);

    }

    @Override
    public void showContactsApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
            WindowAgreement.show(mContext, ContactsApp.class, ContactsApp.id);
        }, Manifest.permission.READ_CONTACTS);

    }

    @Override
    public void showRecorderApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
                    WindowAgreement.show(mContext, RecorderApp.class, -500);
                }, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showCalenderApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, CalenderApp.class, CalenderApp.id);
    }

    @Override
    public void showVolumeApp(FragmentActivity mContext) {
        requestPermission(mContext, new OnResult() {
            @Override
            public void go() {
                WindowAgreement.show(mContext, VolumeApp.class, VolumeApp.id);
            }
        }, Manifest.permission.MODIFY_PHONE_STATE);
    }

    @Override
    public void showFilesApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
                    WindowAgreement.show(mContext, FilesApp.class, FilesApp.id);
                }, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showStopwatchApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, StopwatchApp.class, StopwatchApp.id);
    }

    @Override
    public void showFlashlightApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
                    WindowAgreement.show(mContext, FlashlightApp.class, -1500);
                }, Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showVideoApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
                    WindowAgreement.show(mContext, VideoApp.class, VideoApp.id);
                }, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showGalleryApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
                    WindowAgreement.show(mContext, GalleryApp.class, GalleryApp.id);
                }, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void showLauncherApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
            WindowAgreement.show(mContext, LauncherApp.class, LauncherApp.id);
        }, Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    public void showSystemApp(FragmentActivity mContext) {
        requestPermission(mContext, () -> {
            WindowAgreement.show(mContext, SystemApp.class, new Random().nextInt(Integer.MAX_VALUE));
        }, Manifest.permission.READ_PHONE_STATE);

    }

    @Override
    public void showFacebookApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, FacebookApp.class, FacebookApp.id);
    }

    @Override
    public void showMapsApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, MapsApp.class, MapsApp.id);
    }

    @Override
    public void showGmailApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, GmailApp.class, GmailApp.id);
    }

    @Override
    public void showTwitterApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, TwitterApp.class, TwitterApp.id);
    }

    @Override
    public void showYoutubeApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, YoutubeApp.class, YoutubeApp.id);
    }

    @Override
    public void showBiliBiliApp(FragmentActivity mContext) {
        WindowAgreement.show(mContext, BiliBiliApp.class, BiliBiliApp.id);
    }


    interface OnResult {
        void go();
    }

    private void requestPermission(Context context, OnResult listener, String... permissions) {
        new RxPermissions((FragmentActivity) context).requestEach(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    // 用户已经同意该权限
                    listener.go();
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                    ToastUtil.w_long("没有权限将导致闪退");
                } else {
                    // 用户拒绝了该权限，并且选中『不再询问』
                    ToastUtil.e_long(R.string.permission_read_write_never_ask_again);
                }
            }
        });
    }
}
