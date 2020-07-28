package com.jecelyin.editor.v2.ui.dialog;

import android.content.Context;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jecelyin.editor.v2.Pref;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.ThemeList;
import com.timecat.component.alert.UIUtils;
import com.timecat.component.router.app.NAV;
import com.timecat.component.readonly.RouterHub;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class ChangeThemeDialog extends AbstractDialog {
    public ChangeThemeDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        int length = ThemeList.themes.length;
        final String[] names = new String[length];
        int themeIndex = Pref.getInstance(context).getTheme();

        ThemeList.Theme theme;
        for (int i = 0; i < length; i++) {
            theme = ThemeList.themes[i];
            names[i] = theme.title + (theme.isDark ? " (" + context.getString(R.string.dark) + ")" : "");
        }

        getDialogBuilder()
            .items(names)
            .title(R.string.change_theme)
            .itemsCallbackSingleChoice(themeIndex, new MaterialDialog.ListCallbackSingleChoice() {

                @Override
                public boolean onSelection(MaterialDialog materialDialog, View view, final int i, CharSequence charSequence) {
                    materialDialog.dismiss();
                    UIUtils.showConfirmDialog(context, R.string.confirm_change_theme_message, new UIUtils.OnClickCallback() {
                        @Override
                        public void onOkClick() {
                            Pref.getInstance(context).setTheme(i);
                            restartApp();
                        }
                    });

                    return true;
                }
            })
            .show();
    }

    private void restartApp() {
        NAV.go(RouterHub.EDITOR_RawTextEditorActivity);
    }
}
