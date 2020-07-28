package com.jecelyin.editor.v2.ui.dialog;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jecelyin.editor.v2.R;
import com.timecat.component.data.DBHelper;

import java.util.List;

import com.timecat.component.ui.view.DrawClickableEditText;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class FindKeywordsDialog extends AbstractDialog implements MaterialDialog.SingleButtonCallback, MaterialDialog.ListCallback {
    private final boolean isReplace;
    private final DrawClickableEditText editText;

    public FindKeywordsDialog(Context context, DrawClickableEditText editText, boolean isReplace) {
        super(context);
        this.isReplace = isReplace;
        this.editText = editText;
    }

    @Override
    public void show() {
        List<String> items = DBHelper.getInstance(context).getFindKeywords(isReplace);

        MaterialDialog dlg = getDialogBuilder()
                .items(items)
                .dividerColorRes(R.color.md_divider_black)
                .negativeText(R.string.clear_history)
                .onNegative(this)
                .positiveText(R.string.close)
                .title(isReplace ? R.string.replace_log : R.string.find_log)
                .itemsCallback(this)
                .show();
        handleDialog(dlg);
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        switch (which) {
            case NEGATIVE:
                DBHelper.getInstance(context).clearFindKeywords(isReplace);
            default:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        dialog.dismiss();
        editText.setText(text.toString());
    }
}
