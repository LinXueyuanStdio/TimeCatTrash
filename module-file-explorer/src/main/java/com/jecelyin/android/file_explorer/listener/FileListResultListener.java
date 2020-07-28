package com.jecelyin.android.file_explorer.listener;

import com.jecelyin.android.file_explorer.io.JecFile;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public interface FileListResultListener {
    void onResult(JecFile[] result);
    void onError(String error);
}