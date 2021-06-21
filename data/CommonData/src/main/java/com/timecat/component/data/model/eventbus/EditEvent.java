package com.timecat.component.data.model.eventbus;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/11/28
 * @description null
 * @usage 其他页面发送这个 event 到EditFragment，EditFragment 会处理 content
 */
public class EditEvent {
    public String content;

    public EditEvent(String content) {
        this.content = content;
    }
}
