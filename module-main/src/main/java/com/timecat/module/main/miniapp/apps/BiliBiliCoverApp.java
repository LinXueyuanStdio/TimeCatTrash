package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.timecat.component.alert.ToastUtil;
import com.timecat.component.data.model.events.BiliBiliEvent;
import com.timecat.component.setting.DEF;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BiliBiliCoverApp extends GalleryApp {
    public static int _id = 18;

    public String getAppName() {
        return getString(R.string.main_miniapp_bilibili);
    }

    public int getAppIcon() {
        return R.drawable.ic_bilibili_pink_24dp;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_bilibili);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_bilibili);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.drawable.ic_bilibili_pink_24dp;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_bilibili_cover);
    }

    public String getHiddenNotificationMessage(int id) {
        return getString(R.string.main_miniapp_mininized);
    }

    public Intent getHiddenNotificationIntent(int id) {
        return WindowAgreement.getShowIntent(this, getClass(), id);
    }

    public Animation getShowAnimation(int id) {
        if (isExistingId(id)) {
            return AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        }
        return super.getShowAnimation(id);
    }

    public Animation getHideAnimation(int id) {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
    }

    public StandOutLayoutParams getParams(int id, Window window) {
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
        int w = SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH").equals("") ? 200 : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH"));
        int x = SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS"));
        int y = SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS").equals("") ? Integer.MIN_VALUE : (int) Float.parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS"));
        if (h < GeneralUtils.GetDP(window.getContext(), 200)) {
            h = GeneralUtils.GetDP(window.getContext(), 200);
        }
        if (w < GeneralUtils.GetDP(window.getContext(), 200)) {
            w = GeneralUtils.GetDP(window.getContext(), 200);
        }
        return new StandOutLayoutParams(id, w, h, x, y, GeneralUtils.GetDP(window.getContext(), 56), GeneralUtils.GetDP(window.getContext(), 56));
    }

    public int getFlags(int id) {
        return (((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE) | StandOutFlags.FLAG_WINDOW_HIDE_ENABLE) | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP) | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(int id) {
        return new ArrayList<>();
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.app_bilibili, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.BiliBiliCoverMap.put(id, new BiliBiliCoverCreator());
    }

    public class BiliBiliCoverCreator extends GalleryCreator {
        ImageView imageView2;
        ImageButton imgBtnDownload;
        ImageButton imgBtnBack2;
        String url;

        public BiliBiliCoverCreator() {
            super();
            imageView2 = publicView.findViewById(R.id.imageView2);
            imgBtnBack2 = publicView.findViewById(R.id.imageButtonGalleryBack2);
            imgBtnBack2.setOnClickListener(v -> switchView(0));
            imgBtnDownload = publicView.findViewById(R.id.imageButtonGalleryDownload);
            imgBtnDownload.setOnClickListener(v -> onDownload(url));
            switchView(2);
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void onDownload(String url) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                            //保存
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
                            String s = DEF.config().getBilibiliCover() + format.format(new Date()) + ".jpg";
                            File file = new File(s);
                            file.getParentFile().mkdirs();
                            ImageUtils.save(resource, file, Bitmap.CompressFormat.JPEG);
                            ToastUtil.ok(getResources().getString(R.string.save_sd_card));
                        }
                    });
        }

        public void show(String url) {
            switchView(2);
            try {
                Glide.with(context).load(url).into(imageView2);
            } catch (Exception e) {
                ToastUtil.e(e.toString());
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        show(_id);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImgChangeEvent(BiliBiliEvent event) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        if (event.url != null) {
            GeneralUtils.BiliBiliCoverMap.get(_id).setUrl(event.url.getData().getPic());
            GeneralUtils.BiliBiliCoverMap.get(_id).show(event.url.getData().getPic());
        }
    }

    @Override
    public boolean onShow(int id, Window window) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        return super.onShow(id, window);
    }

    @Override
    public boolean onHide(int id, Window window) {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        return super.onHide(id, window);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
