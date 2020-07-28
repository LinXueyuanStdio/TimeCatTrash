package com.jecelyin.editor.v2.common;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public interface ReadFileListener {
    void onStart();
    void onDone(StringBuilder stringBuilder, String encoding, Throwable throwable);
}
