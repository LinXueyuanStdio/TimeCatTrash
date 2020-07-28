package com.jecelyin.editor.v2.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jecelyin.editor.v2.Pref;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class SymbolBarPreference extends MaterialEditTextPreference {

    public SymbolBarPreference(Context context) {
        super(context);
    }

    public SymbolBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SymbolBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onPrepareDialogBuilder(MaterialDialog.Builder builder) {
        builder.autoDismiss(false);
    }

    @Override
    public String getText() {
        String text = super.getText();
        String[] strings = TextUtils.split(text, "\n");
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            string = string.trim();
            if (string.isEmpty())
                continue;
            if (sb.length() > 0)
                sb.append("\n");
            sb.append(string);
        }
        return sb.toString();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        if (which == DialogInterface.BUTTON_POSITIVE) {
            getDialog().dismiss();
        } else {
            getEditText().setText(Pref.VALUE_SYMBOL);
            setText(Pref.VALUE_SYMBOL);
        }
    }

}
