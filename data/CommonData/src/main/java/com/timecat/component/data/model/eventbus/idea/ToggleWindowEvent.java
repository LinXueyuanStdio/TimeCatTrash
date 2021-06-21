package com.timecat.component.data.model.eventbus.idea;

public final class ToggleWindowEvent {
    private final int doWhat;

    public static final int MINI = 0;//贴边迷你
    public static final int LIST = 1;//idea列表
    public static final int BOARD = 2;//面板

    public ToggleWindowEvent(int to) {
        this.doWhat = to;
    }

    public final int getDoWhat() {
        return this.doWhat;
    }
}
