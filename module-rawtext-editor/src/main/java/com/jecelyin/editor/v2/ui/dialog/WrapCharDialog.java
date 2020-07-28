package com.jecelyin.editor.v2.ui.dialog;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.common.Command;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class WrapCharDialog extends AbstractDialog {
    public WrapCharDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        MaterialDialog dlg = getDialogBuilder().items(R.array.wrap_char_list)
                .title(R.string.convert_wrap_char)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        return false;
                    }
                })
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        int index = dialog.getSelectedIndex();
                        if (index < 0)
                            return;
                        String[] chars = new String[]{"\n", "\r\n"};
                        Command command = new Command(Command.CommandEnum.CONVERT_WRAP_CHAR);
                        command.object = chars[index];
                        getMainActivity().doCommand(command);
                    }
                })
                .show();

        handleDialog(dlg);
    }
}
