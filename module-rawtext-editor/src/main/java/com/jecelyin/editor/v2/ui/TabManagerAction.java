package com.jecelyin.editor.v2.ui;

import com.jecelyin.editor.v2.adapter.EditorAdapter;
import com.jecelyin.editor.v2.utils.ExtGrep;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/7/21
 * @description null
 * @usage null
 */
public interface TabManagerAction {
    void newTab();
    EditorAdapter getEditorAdapter();
    void onDocumentChanged(int index);
    boolean newTab(ExtGrep grep);
    boolean newTab(CharSequence content);
}
