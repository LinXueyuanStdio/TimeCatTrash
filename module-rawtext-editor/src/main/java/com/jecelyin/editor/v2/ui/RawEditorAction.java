package com.jecelyin.editor.v2.ui;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jecelyin.editor.v2.common.Command;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/7/21
 * @description null
 * @usage null
 */
public interface RawEditorAction {
    void openFile(String file, String encoding, int line, int column);
    void insertText(CharSequence text);
    void doCommand(Command command);
    void doNextCommand();
    void closeMenu();
    void setSymbolVisibility(boolean b);
    void setMenuStatus(@IdRes int menuResId, int status);
    void setFindFolderCallback(FolderChooserDialog.FolderCallback findFolderCallback);
    void startPickPathActivity(String path, String filename, String encoding);
    TabManagerAction getTabManager();
    String getCurrentLang();
    FragmentActivity getFragmentActivity();
}
