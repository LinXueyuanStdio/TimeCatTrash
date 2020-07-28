package com.jecelyin.android.file_explorer;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileExplorerContext {
    private final Context context;

    public FileExplorerContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public Resources getResources() {
        return context.getResources();
    }
}
