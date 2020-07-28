package com.jecelyin.android.file_explorer;

import androidx.appcompat.view.ActionMode;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public interface FileExplorerView {
    ActionMode startActionMode(ActionMode.Callback callback);

    void setSelectAll(boolean checked);

    void refresh();

    void finish();
}
