package com.jecelyin.editor.v2.ui.dialog;

import android.content.Context;
import android.view.inputmethod.EditorInfo;

import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.common.Command;
import com.timecat.component.commonsdk.utils.override.L;
import com.timecat.component.commonsdk.utils.string.StringUtils;

import com.timecat.component.alert.UIUtils;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class GotoLineDialog extends AbstractDialog {
    public GotoLineDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        UIUtils.showInputDialog(context, R.string.goto_line, 0, null, EditorInfo.TYPE_CLASS_NUMBER, new UIUtils.OnShowInputCallback() {
            @Override
            public void onConfirm(CharSequence input) {
                try {
                    int line = StringUtils.toInt(input.toString());
                    Command command = new Command(Command.CommandEnum.GOTO_LINE);
                    command.args.putInt("line", line);
                    getMainActivity().doCommand(command);
                } catch (Exception e) {
                    L.e(e);
                }
            }
        });
    }
}
