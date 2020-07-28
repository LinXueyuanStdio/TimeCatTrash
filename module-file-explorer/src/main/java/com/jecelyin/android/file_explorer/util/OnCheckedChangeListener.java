package com.jecelyin.android.file_explorer.util;

import com.jecelyin.android.file_explorer.io.JecFile;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public interface OnCheckedChangeListener {
    void onCheckedChanged(JecFile file, int position, boolean checked);
    void onCheckedChanged(int checkedCount);
}
