package com.timecat.module.main.miniapp.apps;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.timecat.component.alert.ToastUtil;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.module.main.R;
import com.timecat.module.main.miniapp.utilities.GeneralUtils;
import com.timecat.module.main.miniapp.utilities.SettingsUtils;
import com.timecat.plugin.window.StandOutFlags;
import com.timecat.plugin.window.StandOutWindow;
import com.timecat.plugin.window.Window;
import com.timecat.plugin.window.WindowAgreement;

import java.util.ArrayList;
import java.util.List;

public class FlashlightApp extends StandOutWindow {
    Camera camera;
    FlashlightCreator flashlight;
    private Context context;
    private int publicId;
    private View publicView;

    @Override
    public String getAppName() {
        return getString(R.string.main_miniapp_flashlight);
    }

    @Override
    public int getAppIcon() {
        return R.drawable.ic_window_menu;
    }

    @Override
    public String getTitle(int id) {
        return getString(R.string.main_miniapp_flashlight);
    }

    @Override
    public String getPersistentNotificationTitle(int id) {
        return getString(R.string.main_miniapp_flashlight);
    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        return getString(R.string.main_miniapp_running);
    }

    @Override
    public int getHiddenIcon() {
        return R.mipmap.flashlight;
    }

    @Override
    public String getHiddenNotificationTitle(int id) {
        return getString(R.string.main_miniapp_flashlight);
    }

    @Override
    public String getHiddenNotificationMessage(int id) {
        return getString(R.string.main_miniapp_mininized);
    }

    @Override
    public Intent getHiddenNotificationIntent(int id) {
        return WindowAgreement.getShowIntent(this, getClass(), id);
    }

    @Override
    public Animation getShowAnimation(int id) {
        if (isExistingId(id)) {
            return AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        }
        return super.getShowAnimation(id);
    }

    @Override
    public Animation getHideAnimation(int id) {
        return AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
    }

    @Override
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
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_flashlight, frame, true);
        this.publicId = id;
        this.publicView = view;
        this.context = getApplicationContext();
        this.flashlight = new FlashlightCreator();
    }

    public void exitFlashlight() {
        this.flashlight.changeFlashLight(false);
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
        }
    }

    @Override
    public boolean onClose(int id, Window window) {
        exitFlashlight();
        return super.onClose(id, window);
    }

    public class FlashlightCreator {
        boolean hasFlash;
        ImageView imgFlash;
        boolean isFlashOn;
        Parameters params;

        public FlashlightCreator() {
            this.imgFlash = publicView.findViewById(R.id.imageViewFlash);
            this.hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            if (!this.hasFlash) {
                ToastUtil.w(context, "本设备没有闪光灯");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.isFlashOn = false;
                this.imgFlash.setOnClickListener(v -> {
                    changeFlashLight(isFlashOn);
                });
            } else {
                getCamera();
                this.isFlashOn = camera != null && Parameters.FLASH_MODE_TORCH.equals(camera.getParameters().getFlashMode());
                if (!this.isFlashOn) {
                    turnOnFlash();
                }
                this.imgFlash.setOnClickListener(v -> {
                    changeFlashLightOld(isFlashOn);
                });
            }
        }

        void getCamera() {
            if (camera == null) {
                try {
                    camera = Camera.open();
                    this.params = camera.getParameters();
                } catch (RuntimeException e) {
                    ToastUtil.e("请先授权！");
                    e.printStackTrace();
                }
            }
            if (camera == null) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    LogUtil.se("无权限");
                }
            }
        }

        public void changeFlashLightOld(boolean open) {
            LogUtil.se("");
            if (open) {
                turnOffFlash();
            } else {
                turnOnFlash();
            }
        }

        void turnOnFlash() {
            if (!this.isFlashOn && camera != null && this.params != null) {
                this.params = camera.getParameters();
                this.params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(this.params);
                ToastUtil.e("闪光灯：已打开");
                camera.startPreview();
                this.isFlashOn = true;
                this.imgFlash.setImageResource(R.mipmap.flashlight_on);
            }
        }

        void turnOffFlash() {
            if (this.isFlashOn && camera != null && this.params != null) {
                this.params = camera.getParameters();
                this.params.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(this.params);
                ToastUtil.e("闪光灯：已关闭");
                camera.stopPreview();
                this.isFlashOn = false;
                this.imgFlash.setImageResource(R.mipmap.flashlight_off);
            }
        }

        @TargetApi(Build.VERSION_CODES.N)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void changeFlashLight(boolean open) {
            //判断API是否大于24（安卓7.0系统对应的API）
            LogUtil.se(open);
            try {
                //获取CameraManager
                LogUtil.se("获取CameraManager");
                CameraManager mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                //获取当前手机所有摄像头设备ID
                LogUtil.se("获取当前手机所有摄像头设备ID");
                String[] ids = mCameraManager.getCameraIdList();
                LogUtil.se(ids);
                for (String id : ids) {
                    CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                    LogUtil.se("");
                    //查询该摄像头组件是否包含闪光灯
                    Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                    if (flashAvailable != null
                            && flashAvailable
                            && lensFacing != null
                            && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        //打开或关闭手电筒
                        LogUtil.se("");
                        mCameraManager.setTorchMode(id, open);
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
                ToastUtil.w("闪光灯被其他应用占用，请稍后重试");
            }
            LogUtil.se("");
            this.isFlashOn = !open;
            if (open) {
                this.imgFlash.setImageResource(R.mipmap.flashlight_on);
            } else {
                this.imgFlash.setImageResource(R.mipmap.flashlight_off);
            }
        }
    }
}
