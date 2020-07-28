package com.jecelyin.editor.v2.common;

import android.os.Bundle;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class Command {
    public enum CommandEnum {
        NONE,
        HIDE_SOFT_INPUT,
        SHOW_SOFT_INPUT,
        SAVE,
        SAVE_AS,
        OPEN,
        REDO,
        UNDO,
        CUT,
        COPY,
        PASTE,
        SELECT_ALL,
        DUPLICATION,
        WRAP_LINE,
        CONVERT_WRAP_CHAR,
        GOTO_LINE,
        FIND,
        GOTO_TOP,
        GOTO_END,
        BACK,
        FORWARD,
        DOC_INFO,
        READONLY_MODE,
        ENABLE_HIGHLIGHT,
        CHANGE_MODE,
        INSERT_TEXT,
        RELOAD_WITH_ENCODING,
        FULL_SCREEN,
        THEME,
    }

    public CommandEnum what;
    public Bundle args = new Bundle();
    public Object object;

    public Command(CommandEnum what) {
        this.what = what;
    }
}
