package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.util.ArrayList;
import java.util.List;

public class VolumeApp extends StandOutWindow {
    public static int id = 16;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Volume);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Volume);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Volume);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.volume;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Volume);
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
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_volume, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        VolumeCreator volume = new VolumeCreator();
    }

    public class VolumeCreator {
        AudioManager audioManager;
        int currentAlarm;
        int currentCall;
        int currentMedia;
        int currentNotification;
        int currentRing;
        int currentSystem;
        int maxAlarm;
        int maxCall;
        int maxMedia;
        int maxNotification;
        int maxRing;
        int maxSystem;
        SeekBar sbAlarm;
        SeekBar sbCall;
        SeekBar sbMedia;
        SeekBar sbNotification;
        SeekBar sbRingtone;
        SeekBar sbSystem;

        public VolumeCreator() {
            this.audioManager = (AudioManager) VolumeApp.this.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) {
                ToastUtil.e("请求被拒绝！");
                return;
            }
            maxAlarm = this.audioManager.getStreamMaxVolume(4);
            maxCall = this.audioManager.getStreamMaxVolume(0);
            maxMedia = this.audioManager.getStreamMaxVolume(3);
            maxNotification = this.audioManager.getStreamMaxVolume(5);
            maxRing = this.audioManager.getStreamMaxVolume(2);
            maxSystem = this.audioManager.getStreamMaxVolume(1);
            this.sbRingtone = (SeekBar) VolumeApp.this.publicView.findViewById(R.id.seekBarRingtone);
            this.sbMedia = (SeekBar) VolumeApp.this.publicView.findViewById(R.id.seekBarMedia);
            this.sbNotification = (SeekBar) VolumeApp.this.publicView.findViewById(R.id.seekBarNotification);
            this.sbSystem = (SeekBar) VolumeApp.this.publicView.findViewById(R.id.seekBarSystem);
            this.sbAlarm = (SeekBar) VolumeApp.this.publicView.findViewById(R.id.seekBarAlarm);
            this.sbCall = (SeekBar) VolumeApp.this.publicView.findViewById(R.id.seekBarCall);
            this.sbRingtone.setMax(this.maxRing);
            this.sbMedia.setMax(this.maxMedia);
            this.sbNotification.setMax(this.maxNotification);
            this.sbSystem.setMax(this.maxSystem);
            this.sbAlarm.setMax(this.maxAlarm);
            this.sbCall.setMax(this.maxCall);
            this.currentRing = this.audioManager.getStreamVolume(2);
            this.currentMedia = this.audioManager.getStreamVolume(3);
            this.currentNotification = this.audioManager.getStreamVolume(5);
            this.currentSystem = this.audioManager.getStreamVolume(1);
            this.currentAlarm = this.audioManager.getStreamVolume(4);
            this.currentCall = this.audioManager.getStreamVolume(0);
            this.sbRingtone.setProgress(this.currentRing);
            this.sbMedia.setProgress(this.currentMedia);
            this.sbNotification.setProgress(this.currentNotification);
            this.sbSystem.setProgress(this.currentSystem);
            this.sbAlarm.setProgress(this.currentAlarm);
            this.sbCall.setProgress(this.currentCall);
            this.sbRingtone.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(2, progress, 0);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            this.sbMedia.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(3, progress, 0);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            this.sbNotification.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(5, progress, 0);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            this.sbSystem.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(1, progress, 0);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            this.sbAlarm.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(4, progress, 0);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            this.sbCall.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(0, progress, 0);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }
    }
}
