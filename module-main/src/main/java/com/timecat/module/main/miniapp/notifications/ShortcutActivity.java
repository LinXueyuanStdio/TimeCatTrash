package com.timecat.module.main.miniapp.notifications;

import com.alibaba.android.arouter.launcher.ARouter;
import com.timecat.component.commonbase.base.BaseNoDisplayActivity;
import com.timecat.component.data.service.WindowService;
import com.timecat.component.readonly.RouterHub;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;

public abstract class ShortcutActivity extends BaseNoDisplayActivity {
    abstract int getIndex();

    protected void run() {
        Object o = ARouter.getInstance().build(RouterHub.GLOBAL_WindowServiceImpl)
                .navigation(this);
        if (o == null) return;
        WindowService windowService = (WindowService) o;
        int i = GeneralUtils.GetNotificationIcon(this, getIndex());
        if (i == R.mipmap.browser) {
            windowService.showBrowserApp(this);
        } else if (i == R.mipmap.calculator) {
            windowService.showCalculatorApp(this);

        } else if (i == R.mipmap.calender) {
            windowService.showCalenderApp(this);

        } else if (i == R.mipmap.camera) {
            windowService.showCameraApp(this);

        } else if (i == R.mipmap.contacts) {
            windowService.showContactsApp(this);

        } else if (i == R.mipmap.dialer) {
            windowService.showDialerApp(this);

        } else if (i == R.mipmap.facebook) {
            windowService.showFacebookApp(this);

        } else if (i == R.mipmap.files) {
            windowService.showFilesApp(this);

        } else if (i == R.mipmap.flashlight) {
            windowService.showFlashlightApp(this);

        } else if (i == R.mipmap.gallery) {
            windowService.showGalleryApp(this);

        } else if (i == R.mipmap.gmail) {
            windowService.showGmailApp(this);

        } else if (i == R.mipmap.launcher) {
            windowService.showLauncherApp(this);

        } else if (i == R.mipmap.maps) {
            windowService.showMapsApp(this);

        } else if (i == R.mipmap.notes) {
            windowService.showNotesApp(this);

        } else if (i == R.mipmap.paint) {
            windowService.showPaintApp(this);

        } else if (i == R.mipmap.player) {
            windowService.showMusicApp(this);

        } else if (i == R.mipmap.recorder) {
            windowService.showRecorderApp(this);

        } else if (i == R.mipmap.stopwatch) {
            windowService.showStopwatchApp(this);

        } else if (i == R.mipmap.system) {
            windowService.showSystemApp(this);

        } else if (i == R.mipmap.twitter) {
            windowService.showTwitterApp(this);

        } else if (i == R.mipmap.video) {
            windowService.showVideoApp(this);

        } else if (i == R.mipmap.volume) {
            windowService.showVolumeApp(this);

        } else if (i == R.mipmap.youtube) {
            windowService.showYoutubeApp(this);

        } else if (i == R.drawable.ic_bilibili_pink_24dp) {
            windowService.showBiliBiliApp(this);
        }
    }
}
