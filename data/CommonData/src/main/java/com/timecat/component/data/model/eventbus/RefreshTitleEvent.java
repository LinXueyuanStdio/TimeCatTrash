package com.timecat.component.data.model.eventbus;

public class RefreshTitleEvent {
    public String title;
    public int contentLength;

    public RefreshTitleEvent(String title, int len) {
        this.title = title;
        this.contentLength = len;
    }
}
