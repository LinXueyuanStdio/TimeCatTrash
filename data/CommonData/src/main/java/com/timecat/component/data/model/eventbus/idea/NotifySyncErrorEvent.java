package com.timecat.component.data.model.eventbus.idea;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
public class NotifySyncErrorEvent {
    private final Integer code;
    private final String msg;

    public NotifySyncErrorEvent(Integer num, String str) {
        this.code = num;
        this.msg = str;
    }

    public final Integer getCode() {
        return this.code;
    }

    public final String getMsg() {
        return this.msg;
    }
}
