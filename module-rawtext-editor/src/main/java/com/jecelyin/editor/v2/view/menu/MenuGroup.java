package com.jecelyin.editor.v2.view.menu;


import com.jecelyin.editor.v2.R;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public enum MenuGroup {
    TOP(0),
    FILE(R.string.file),
    EDIT(R.string.edit),
    FIND(R.string.find),
    VIEW(R.string.view),
    OTHER(R.string.other);

    private int nameResId;

    MenuGroup(int resId) {
        nameResId = resId;
    }

    public int getNameResId() {
        return nameResId;
    }
}
