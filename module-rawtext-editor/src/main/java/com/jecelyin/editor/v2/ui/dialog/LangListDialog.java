package com.jecelyin.editor.v2.ui.dialog;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.common.Command;
import com.jecelyin.editor.v2.ui.ModeList;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class LangListDialog extends AbstractDialog {
    private String[] langList;
    private int currentLangIndex = -1;

    public LangListDialog(Context context) {
        super(context);

        String currLang = getMainActivity().getCurrentLang();

        int length = ModeList.modes.length;
        langList = new String[length];
        for (int i = 0; i < length; i++) {
            langList[i] = ModeList.modes[i].name;
            if (currLang != null && currLang.equals(langList[i])) {
                currentLangIndex = i;
            }
        }
    }

    @Override
    public void show() {
        MaterialDialog dlg = getDialogBuilder().items(langList)
                .title(R.string.select_lang_to_highlight)
                .itemsCallbackSingleChoice(currentLangIndex, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        Command command = new Command(Command.CommandEnum.CHANGE_MODE);
                        command.object = ModeList.modes[i].mode;
                        getMainActivity().doCommand(command);
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .show();

        handleDialog(dlg);
    }
}
