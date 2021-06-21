package com.timecat.component.data.model.eventbus;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-30
 * @description null
 * @usage null
 */
public class TabReselectedEvent {
    int position = 0;

    public TabReselectedEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
