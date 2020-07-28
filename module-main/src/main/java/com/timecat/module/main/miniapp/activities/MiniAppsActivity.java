package com.timecat.module.main.miniapp.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.commonbase.friend.list.BaseToolbarActivity;
import com.timecat.component.data.service.WindowService;
import com.timecat.component.readonly.Constants;
import com.timecat.component.readonly.RouterHub;
import com.timecat.component.setting.DEF;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.adapters.GridItemAdapter;
import com.timecat.module.main.miniapp.models.GridItemModel;
import com.timecat.module.main.miniapp.notifications.MiniAppNotification;

import java.util.ArrayList;

@Route(path = RouterHub.MAIN_MiniAppsActivity)
public class MiniAppsActivity extends BaseToolbarActivity {

    ArrayList<GridItemModel> arrayListApps;
    Context context = this;
    GridView gridViewApps;
    GridItemAdapter listItemAdapterApps;
    @Autowired(name = RouterHub.GLOBAL_WindowServiceImpl)
    WindowService windowService;
    @NonNull
    @Override
    protected String title() {
        return getString(R.string.drawer_mini_apps_option);
    }

    @Override
    protected int layout() {
        return R.layout.activity_toolbox;
    }

    @Override
    protected void routerInject() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initView() {
        gridViewApps = findViewById(R.id.gridViewApps);
        arrayListApps = new ArrayList<>();
        arrayListApps.add(new GridItemModel(R.mipmap.calculator, "计算器"));
        arrayListApps.add(new GridItemModel(R.mipmap.notes, "笔记"));
        arrayListApps.add(new GridItemModel(R.mipmap.paint, "画板"));
        arrayListApps.add(new GridItemModel(R.mipmap.player, "音乐播放器(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.browser, "浏览器"));
        arrayListApps.add(new GridItemModel(R.mipmap.camera, "照相机(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.dialer, "电话(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.contacts, "联系人(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.recorder, "录音(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.calender, "日历"));
        arrayListApps.add(new GridItemModel(R.mipmap.volume, "音量(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.files, "文件浏览器(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.stopwatch, "倒计时"));
        arrayListApps.add(new GridItemModel(R.mipmap.flashlight, "手电筒(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.video, "视频播放器(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.gallery, "图片浏览器(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.launcher, "应用启动器(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.system, "关于系统(需要权限)"));
        arrayListApps.add(new GridItemModel(R.mipmap.facebook, "facebook"));
        arrayListApps.add(new GridItemModel(R.mipmap.maps, "地图"));
        arrayListApps.add(new GridItemModel(R.mipmap.gmail, "gmail"));
        arrayListApps.add(new GridItemModel(R.mipmap.twitter, "twitter"));
        arrayListApps.add(new GridItemModel(R.mipmap.youtube, "youtube"));
        arrayListApps.add(new GridItemModel(R.drawable.ic_bilibili_pink_24dp, "BiliBili"));
        arrayListApps.add(new GridItemModel(R.mipmap.settings, "设置零应用"));
        listItemAdapterApps = new GridItemAdapter(this.arrayListApps, this, R.layout.layout_griditem);
        gridViewApps.setAdapter(listItemAdapterApps);
        gridViewApps.setOnItemClickListener((parent, view, position, id) -> {
            if (windowService == null) return;
            switch (position) {
                case 0:
                    windowService.showCalculatorApp(this);
                    break;
                case 1:
                    windowService.showNotesApp(this);
                    break;
                case 2:
                    windowService.showPaintApp(this);
                    break;
                case 3:
                    windowService.showMusicApp(this);
                    break;
                case 4:
                    windowService.showBrowserApp(this);
                    break;
                case 5:
                    windowService.showCameraApp(this);
                    break;
                case 6:
                    windowService.showDialerApp(this);
                    break;
                case 7:
                    windowService.showContactsApp(this);
                    break;
                case 8:
                    windowService.showRecorderApp(this);
                    break;
                case 9:
                    windowService.showCalenderApp(this);
                    break;
                case 10:
                    windowService.showVolumeApp(this);
                    break;
                case 11:
                    windowService.showFilesApp(this);
                    break;
                case 12:
                    windowService.showStopwatchApp(this);
                    break;
                case 13:
                    windowService.showFlashlightApp(this);
                    break;
                case 14:
                    windowService.showVideoApp(this);
                    break;
                case 15:
                    windowService.showGalleryApp(this);
                    break;
                case 16:
                    windowService.showLauncherApp(this);
                    break;
                case 17:
                    windowService.showSystemApp(this);
                    break;
                case 18:
                    windowService.showFacebookApp(this);
                    break;
                case 19:
                    windowService.showMapsApp(this);
                    break;
                case 20:
                    windowService.showGmailApp(this);
                    break;
                case 21:
                    windowService.showTwitterApp(this);
                    break;
                case 22:
                    windowService.showYoutubeApp(this);
                    break;
                case 23:
                    windowService.showBiliBiliApp(this);
                    break;
                case 24:
                    startActivity(new Intent(context, SettingMiniAppActivity.class));
                    break;
            }
            finish();
        });
        if (DEF.config().getBoolean(Constants.IS_SHOW_APP_SET_NOTIFY, false)) {
            MiniAppNotification.startNotification(this);
        }
        setFinishOnTouchOutside(true);
    }

}
