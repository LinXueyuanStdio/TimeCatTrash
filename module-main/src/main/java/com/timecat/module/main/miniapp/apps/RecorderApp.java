package com.timecat.module.main.miniapp.apps;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecorderApp extends StandOutWindow {
    Handler mHandler;
    MediaRecorder recorder;
    private Context context;
    private int publicId;
    private View publicView;

    public String getAppName() {
        return getString(R.string.main_miniapp_Recorder);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_Recorder);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Recorder);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.recorder;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_Recorder);
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
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_recorder, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        RecorderCreator recorder = new RecorderCreator();
    }

    public class RecorderCreator {
        long currentDuration = 0;
        EditText editTextFilename;
        ImageButton imgBtnRecord;
        boolean isRecording = false;
        TextView tvTime;
        private Runnable mUpdateTimeTask = new Runnable() {
            @Override
            public void run() {
                RecorderCreator recorderCreator = RecorderCreator.this;
                recorderCreator.currentDuration += 1000;
                tvTime.setText("" + GeneralUtils.milliSecondsToTimer(currentDuration));
                mHandler.postDelayed(this, 1000);
            }
        };

        public RecorderCreator() {
            new File(GeneralUtils.GetRecorderFolderPath()).mkdirs();
            this.tvTime = (TextView) publicView.findViewById(R.id.textViewSongTime);
            this.editTextFilename = (EditText) publicView.findViewById(R.id.editTextFilename);
            this.imgBtnRecord = (ImageButton) publicView.findViewById(R.id.imageButtonRecord);
            this.imgBtnRecord.setOnClickListener(v -> {
                if (isRecording) {
                    stopRecording();
                } else if (editTextFilename.getText().toString().trim().equals("")) {
                    ToastUtil.i(context, "Please enter file name");
                } else {
                    startRecording();
                }
            });
            mHandler = new Handler();
        }

        void startRecording() {
            if (recorder != null) {
                recorder.release();
            }
            this.isRecording = true;
            recorder = new MediaRecorder();
            recorder.setAudioSource(1);
            recorder.setOutputFormat(1);
            recorder.setAudioEncoder(1);
            recorder.setOutputFile(GeneralUtils.GetRecorderFolderPath() + this.editTextFilename.getText() + ".3gp");
            try {
                recorder.prepare();
                recorder.start();
                this.imgBtnRecord.setImageResource(R.mipmap.menu_pause);
                updateProgressBar();
            } catch (IOException e) {
            }
        }

        void stopRecording() {
            if (recorder != null) {
                this.currentDuration = 0;
                this.tvTime.setText("" + GeneralUtils.milliSecondsToTimer(this.currentDuration));
                mHandler.removeCallbacks(this.mUpdateTimeTask);
                this.isRecording = false;
                recorder.stop();
                this.imgBtnRecord.setImageResource(R.mipmap.menu_play);
                recorder.release();
                recorder = null;
                ToastUtil.i(context, "File saved");
            }
        }

        public void updateProgressBar() {
            mHandler.postDelayed(this.mUpdateTimeTask, 1000);
        }
    }
}
