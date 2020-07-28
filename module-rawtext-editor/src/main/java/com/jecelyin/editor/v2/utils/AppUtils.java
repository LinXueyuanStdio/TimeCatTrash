package com.jecelyin.editor.v2.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Base64;

import com.timecat.component.commonsdk.utils.override.L;
import com.timecat.component.commonsdk.utils.utils.SysUtils;
import com.timecat.component.alert.UIUtils;
import com.jecelyin.editor.v2.R;

import java.security.MessageDigest;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class AppUtils {
    private final static String rightSign = "u+AXcAUmNluGqVoNFQCe4+o6BLc=\n";

    public static boolean verifySign(Context context) {
        try {
            byte[] signature = SysUtils.getSignature(context);
            if (signature == null)
                return false;
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature);
            final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            return rightSign.equals(currentSignature);
        } catch (Exception e) {
            L.e(e);
            return false;
        }
    }

    public static void showException(Context context, String code, @NonNull final Exception e) {
        UIUtils.showConfirmDialog(context, context.getString(R.string.unknown_exception_and_report_message_x, code), new UIUtils.OnClickCallback() {
            @Override
            public void onOkClick() {

            }
        });
    }
}
