package com.timecat.component.data.model.eventbus;

import com.timecat.component.data.model.api.BiliBiliVideo;

public class BiliBiliEvent {
    public BiliBiliVideo url;

    public BiliBiliEvent(BiliBiliVideo url) {
        this.url = url;
    }
}
