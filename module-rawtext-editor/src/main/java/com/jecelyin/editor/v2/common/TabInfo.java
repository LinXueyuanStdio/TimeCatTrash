package com.jecelyin.editor.v2.common;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class TabInfo {
    private String title;
    private String path;
    private boolean hasChanged;

    public TabInfo(String title, String file, boolean hasChanged) {
        this.title = title;
        this.path = file;
        this.hasChanged = hasChanged;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public boolean hasChanged() {
        return hasChanged;
    }
}
