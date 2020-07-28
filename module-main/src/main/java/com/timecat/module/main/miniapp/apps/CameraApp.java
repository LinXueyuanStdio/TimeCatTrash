package com.timecat.module.main.miniapp.apps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.others.CameraSurfaceView;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CameraApp extends StandOutWindow {

    private Context context;
    private int publicId;
    private View publicView;
    private ConfigurationChangeBroadcast broadcast;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcast = new ConfigurationChangeBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        registerReceiver(broadcast, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcast != null) {
            unregisterReceiver(broadcast);
        }
    }

    public String getAppName() {
        return getString(R.string.main_miniapp_camera);
    }

    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    public String getTitle(int id) {
        return getString(R.string.main_miniapp_camera);
    }

    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_camera);
    }

    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    public int getHiddenIcon() {
        return R.mipmap.camera;
    }

    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_camera);
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
        int h = SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT").equals("") ? 200
                : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "HEIGHT"));
        int w = SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH").equals("") ? 200
                : Integer.parseInt(SettingsUtils.GetValue(window.getContext(), getAppName() + "WIDTH"));
        int x = SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS").equals("")
                ? Integer.MIN_VALUE : (int) Float
                .parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "XPOS"));
        int y = SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS").equals("")
                ? Integer.MIN_VALUE : (int) Float
                .parseFloat(SettingsUtils.GetValue(window.getContext(), getAppName() + "YPOS"));
        if (h < GeneralUtils.GetDP(window.getContext(), 200)) {
            h = GeneralUtils.GetDP(window.getContext(), 200);
        }
        if (w < GeneralUtils.GetDP(window.getContext(), 200)) {
            w = GeneralUtils.GetDP(window.getContext(), 200);
        }
        return new StandOutLayoutParams(id, w, h, x, y, GeneralUtils.GetDP(window.getContext(), 56),
                GeneralUtils.GetDP(window.getContext(), 56));
    }

    public int getFlags(int id) {
        return ((StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_BODY_MOVE_ENABLE)
                | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP)
                | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
    }

    public List<DropDownListItem> getDropDownItems(int id) {
        List<DropDownListItem> items = new ArrayList<>();
        items.add(new DropDownListItem(R.mipmap.flashlight_off, getString(R.string.main_miniapp_FlashONOFF),
                new Runnable() {
                    @Override
                    public void run() {
                        if (SettingsUtils.GetValue(context, "CAMERA_FLASH").equals("") || SettingsUtils
                                .GetValue(context, "CAMERA_FLASH").equals("YES")) {
                            SettingsUtils.SetValue(context, "CAMERA_FLASH", "NO");
                            ToastUtil.i(context, "Flash Off");
                            return;
                        }
                        SettingsUtils.SetValue(context, "CAMERA_FLASH", "YES");
                        ToastUtil.i(context, "Flash On");
                    }
                }));
        return items;
    }

    public void createAndAttachView(int id, FrameLayout frame) {
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.app_camera, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        GeneralUtils.CameraObject = new CameraCreator();
    }

    public class CameraCreator implements PictureCallback {

        public CameraSurfaceView cameraSurfaceView;
        boolean flash = false;
        FrameLayout preview;
        TextView textViewSaving;
        private ImageView imgViewClick;

        public CameraCreator() {
            this.imgViewClick = (ImageView) publicView.findViewById(R.id.imageViewCameraClick);
            this.preview = (FrameLayout) publicView.findViewById(R.id.camera_preview);
            this.textViewSaving = (TextView) publicView.findViewById(R.id.textViewSaving);
            try {
                this.cameraSurfaceView = new CameraSurfaceView(context);
                this.preview.addView(this.cameraSurfaceView);
                this.imgViewClick.setOnClickListener(v -> takePicture());
            } catch (Exception e) {
                ToastUtil.i(getApplicationContext(), "Unable to access camera");
            }
        }

        private void takePicture() {
            if (null == cameraSurfaceView.getCamera()) {
                ToastUtil.e("打不开...去[主页侧边栏 > 权限]看看有没有权限喵~");
                return;
            }
            if (SettingsUtils.GetValue(context, "CAMERA_FLASH").equals("")
                    || SettingsUtils.GetValue(context, "CAMERA_FLASH").equals("YES")) {
                cameraSurfaceView.setFlashMode("on");
                this.flash = true;
            } else {
                cameraSurfaceView.setFlashMode("off");
                this.flash = false;
            }
            this.cameraSurfaceView.takePicture(this);
            Vibrator v = ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE));
            if (v != null) {
                v.vibrate(50);
            }
        }

        public void onPictureTaken(byte[] data, Camera camera) {
            new TakePictureAsyncTask().execute(data, camera);
        }

        class TakePictureAsyncTask extends AsyncTask<Object, Void, String> {

            Camera camera;
            byte[] data;

            TakePictureAsyncTask() {
            }

            protected String doInBackground(Object... params) {
                this.data = (byte[]) params[0];
                this.camera = (Camera) params[1];
                try {
                    Bitmap bmp = BitmapFactory.decodeByteArray(this.data, 0, this.data.length);
                    Matrix matrix = new Matrix();
                    if (getResources().getConfiguration().orientation != 2) {
                        matrix.postRotate(90.0f);
                    }
                    Bitmap resizedBitmap = Bitmap
                            .createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resizedBitmap.compress(CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    File camImages = new File(GeneralUtils.GetCameraFolderPath());
                    camImages.mkdirs();
                    FileOutputStream outStream = new FileOutputStream(
                            new File(camImages, "CAM_" + System.currentTimeMillis() + ".jpg"));
                    try {
                        outStream.write(byteArray);
                        outStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startPreview();
                return "";
            }

            private void startPreview() {
                try {
                    this.camera.startPreview();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }

            protected void onPostExecute(String result) {
                imgViewClick.setVisibility(View.VISIBLE);
                textViewSaving.setVisibility(View.INVISIBLE);
            }

            protected void onPreExecute() {
                imgViewClick.setVisibility(View.INVISIBLE);
                textViewSaving.setVisibility(View.VISIBLE);
            }
        }
    }

    class ConfigurationChangeBroadcast extends BroadcastReceiver {

        ConfigurationChangeBroadcast() {
        }

        public void onReceive(Context context, Intent myIntent) {
            if (!"android.intent.action.CONFIGURATION_CHANGED".equals(myIntent.getAction())) return;
            if (GeneralUtils.CameraObject == null) return;

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GeneralUtils.CameraObject.cameraSurfaceView.toLandscape();
            } else {
                GeneralUtils.CameraObject.cameraSurfaceView.toPotrait();
            }
        }
    }

}
