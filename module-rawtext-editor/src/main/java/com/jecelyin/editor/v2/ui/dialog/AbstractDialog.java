package com.jecelyin.editor.v2.ui.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jecelyin.editor.v2.ui.EditorActivity;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public abstract class AbstractDialog {
    protected final Context context;

    public AbstractDialog(Context context) {
        this.context = context;
    }

    protected MaterialDialog.Builder getDialogBuilder() {
        return new MaterialDialog.Builder(context);
    }

    protected void handleDialog(MaterialDialog dlg) {
        dlg.setCanceledOnTouchOutside(false);
        dlg.setCancelable(true);
    }

    protected EditorActivity getMainActivity() {
        return (EditorActivity) context;
    }

    abstract public void show();
}
