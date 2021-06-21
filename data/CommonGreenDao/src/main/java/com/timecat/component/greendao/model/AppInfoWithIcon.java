package com.timecat.component.greendao.model;

import android.graphics.Bitmap;

public class AppInfoWithIcon extends AppInfo {
    private Bitmap icon;
    private AppInfo appInfo;

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public AppInfoWithIcon(AppInfo info, Bitmap icon) {
        this.setId(info.getId());
        this.appInfo = info;
        this.enableState = info.enableState;
        this.appType = info.appType;
        this.title = info.title;
        this.packageName = info.packageName;
        this.icon = icon;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }
}
