package com.timecat.module.editor.idea;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.timecat.ui.block.util.DeviceUtil;
import com.timecat.ui.block.util.LocaleUtil;
import com.timecat.ui.block.util.PermissionUtil;


/**
 * Created by ywwynm on 2015/6/4. A base Activity class to reduce same codes in different
 * subclasses.
 */
public abstract class EverythingDoneBaseActivity extends AppCompatActivity {

    public static final String TAG = "EverythingDoneBaseActivity";
    private SparseArray<PermissionUtil.PermissionCallback> mCallbacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call this before setContentView so that NavigationView can update its language correctly.
        LocaleUtil.changeLanguage();

        beforeSetContentView();

        setContentView(getLayoutResource());

        beforeInit();
        init();
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T f(@IdRes int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T f(View v, @IdRes int id) {
        return (T) v.findViewById(id);
    }

    protected void beforeSetContentView() {
    }

    protected abstract @LayoutRes
    int getLayoutResource();

    protected void beforeInit() {
    }

    protected void init() {
        initMembers();
        findViews();
        initUI();
        setActionbar();
        setEvents();
    }

    protected abstract void initMembers();

    protected abstract void findViews();

    protected abstract void initUI();

    protected abstract void setActionbar();

    protected abstract void setEvents();

    public void doWithPermissionChecked(
            @NonNull PermissionUtil.PermissionCallback permissionCallback, int requestCode,
            String... permissions) {
        if (DeviceUtil.hasMarshmallowApi()) {
            if (mCallbacks == null) {
                mCallbacks = new SparseArray<>();
            }
            for (String permission : permissions) {
                int pg = ContextCompat.checkSelfPermission(this, permission);
                if (pg != PackageManager.PERMISSION_GRANTED) {
                    mCallbacks.put(requestCode, permissionCallback);
                    ActivityCompat.requestPermissions(this, permissions, requestCode);
                    return;
                }
            }
        }

        permissionCallback.onGranted();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.PermissionCallback callback = mCallbacks.get(requestCode);
        if (callback != null) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    callback.onDenied();
                    return;
                }
            }
            callback.onGranted();
        }
    }

}
