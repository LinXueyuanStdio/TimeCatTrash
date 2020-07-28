package com.timecat.module.main.miniapp.others;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements Callback {
    @Nullable
    Camera camera;

    @Nullable
    public Camera getCamera() {
        return this.camera;
    }

    public void setCamera(@NonNull Camera camera) {
        this.camera = camera;
    }

    public CameraSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera==null)return;
        try {
            if (getResources().getConfiguration().orientation == 2) {
                this.camera.setDisplayOrientation(0);
            } else {
                this.camera.setDisplayOrientation(90);
            }
            this.camera.startPreview();
        } catch (Exception ignore) {
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            this.camera = Camera.open();
            this.camera.setPreviewDisplay(holder);
            Parameters p = this.camera.getParameters();
            p.setFocusMode("continuous-picture");
            this.camera.setParameters(p);
        } catch (Exception e) {
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera==null)return;
        this.camera.stopPreview();
        this.camera.release();
        this.camera = null;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    public void takePicture(PictureCallback imageCallback) {
        if (camera!=null)
            this.camera.takePicture(null, null, imageCallback);
    }

    public void toLandscape() {
        if (camera!=null)
            this.camera.setDisplayOrientation(0);
    }

    public void toPotrait() {
        if (camera!=null)
        this.camera.setDisplayOrientation(90);
    }

    public void setFlashMode(String s){
        Parameters p;
        p = getCamera().getParameters();
        p.setFlashMode(s);
        getCamera().setParameters(p);
    }
}
