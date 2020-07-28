package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.util.ArrayList;
import java.util.List;

public class StopwatchApp extends StandOutWindow {
    public static int id = 13;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Stopwatch);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Stopwatch);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Stopwatch);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.stopwatch;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Stopwatch);
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
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_stopwatch, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.StopwatchMap.put(Integer.valueOf(id), new StopwatchCreator());
    }

    public class StopwatchCreator {
        ImageButton btnReset;
        ImageButton btnStart;
        long finalTime = 0;
        int started = 0;
        long timeInMillies = 0;
        long timeSwap = 0;
        TextView timer;
        private Handler myHandler = new Handler();
        private long startTime = 0;
        private Runnable updateTimerMethod = new C03233();

        public StopwatchCreator() {
            this.btnReset = (ImageButton) StopwatchApp.this.publicView.findViewById(R.id.buttonReset);
            this.btnStart = (ImageButton) StopwatchApp.this.publicView.findViewById(R.id.buttonStart);
            this.timer = (TextView) StopwatchApp.this.publicView.findViewById(R.id.textViewSWTime);
            this.btnStart.setOnClickListener(view -> {
                if (StopwatchCreator.this.started == 0) {
                    StopwatchCreator.this.started = 1;
                    StopwatchCreator.this.startTime = SystemClock.uptimeMillis();
                    StopwatchCreator.this.myHandler.postDelayed(StopwatchCreator.this.updateTimerMethod, 0);
                    StopwatchCreator.this.btnStart.setImageResource(R.mipmap.menu_pause);
                    return;
                }
                StopwatchCreator.this.started = 0;
                StopwatchCreator stopwatchCreator = StopwatchCreator.this;
                stopwatchCreator.timeSwap += StopwatchCreator.this.timeInMillies;
                StopwatchCreator.this.myHandler.removeCallbacks(StopwatchCreator.this.updateTimerMethod);
                StopwatchCreator.this.btnStart.setImageResource(R.mipmap.menu_play);
            });
            this.btnReset.setOnClickListener(view -> {
                StopwatchCreator.this.timeInMillies = 0;
                StopwatchCreator.this.timeSwap = 0;
                StopwatchCreator.this.finalTime = 0;
                StopwatchCreator.this.startTime = 0;
                StopwatchCreator.this.started = 0;
                StopwatchCreator.this.myHandler.removeCallbacks(StopwatchCreator.this.updateTimerMethod);
                StopwatchCreator.this.timer.setText("00 : 00 : 00.000");
                StopwatchCreator.this.btnStart.setImageResource(R.mipmap.menu_play);
            });
        }

        class C03233 implements Runnable {
            C03233() {
            }

            public void run() {
                StopwatchCreator.this.timeInMillies = SystemClock.uptimeMillis() - StopwatchCreator.this.startTime;
                StopwatchCreator.this.finalTime = StopwatchCreator.this.timeSwap + StopwatchCreator.this.timeInMillies;
                int seconds = (int) (StopwatchCreator.this.finalTime / 1000);
                int minutes = seconds / 60;
                seconds %= 60;
                int hours = minutes / 60;
                minutes %= 60;
                int milliseconds = (int) (StopwatchCreator.this.finalTime % 1000);
                StopwatchCreator.this.timer.setText(String.format("%02d", new Object[]{Integer.valueOf(hours)}) + " : " + String.format("%02d", new Object[]{Integer.valueOf(minutes)}) + " : " + String.format("%02d", new Object[]{Integer.valueOf(seconds)}) + "." + String.format("%03d", new Object[]{Integer.valueOf(milliseconds)}));
                StopwatchCreator.this.myHandler.postDelayed(this, 0);
            }
        }
    }
}
