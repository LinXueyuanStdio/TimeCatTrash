package com.jecelyin.editor.v2.common;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public interface TabCloseListener {
    public void onClose(String path, String encoding, int line, int column);
}
