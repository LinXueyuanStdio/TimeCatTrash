package com.timecat.component.data.model.eventbus;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/11/28
 * @description null
 * @usage 其他页面接收这个 event，从中拿 content 来保存
 */
public class AutoSaveEvent {
    public String title;
    public String content;

    public AutoSaveEvent(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
