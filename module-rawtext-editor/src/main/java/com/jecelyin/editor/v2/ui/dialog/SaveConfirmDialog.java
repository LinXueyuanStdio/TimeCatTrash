package com.jecelyin.editor.v2.ui.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jecelyin.editor.v2.R;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class SaveConfirmDialog extends AbstractDialog {
    private final MaterialDialog.SingleButtonCallback callback;
    private final String filename;

    public SaveConfirmDialog(Context context, String filename, MaterialDialog.SingleButtonCallback callback) {
        super(context);
        this.callback = callback;
        this.filename = filename;
    }

    @Override
    public void show() {
        getDialogBuilder().title(R.string.confirm_save)
                .content(context.getString(R.string.confirm_save_msg, filename))
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .neutralText(R.string.cancel)
                .onPositive(callback)
                .onNegative(callback)
                .show();
    }
}
