package com.timecat.component.data.model.eventbus.idea;

public final class NotifySyncFinishEvent {
    private final boolean dataChanged;

    public NotifySyncFinishEvent(boolean z) {
        this.dataChanged = z;
    }

    public final boolean getDataChanged() {
        return this.dataChanged;
    }
}
