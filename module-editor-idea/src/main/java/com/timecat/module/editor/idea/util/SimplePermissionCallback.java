package com.timecat.module.editor.idea.util;

import com.timecat.component.alert.ToastUtil;
import com.timecat.module.editor.idea.R;
import com.timecat.ui.block.util.PermissionUtil;

/**
 * Created by ywwynm on 2016/5/21. simple permission callback
 */
public class SimplePermissionCallback implements PermissionUtil.PermissionCallback {


    public SimplePermissionCallback() {
    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied() {
        ToastUtil.e(R.string.error_permission_denied);
    }
}
