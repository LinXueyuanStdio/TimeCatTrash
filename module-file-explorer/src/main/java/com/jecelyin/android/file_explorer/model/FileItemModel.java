package com.jecelyin.android.file_explorer.model;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileItemModel {
    private String ext;
    private String date;
    private String name;
    private String secondLine;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondLine() {
        return secondLine;
    }

    public void setSecondLine(String secondLine) {
        this.secondLine = secondLine;
    }
}
