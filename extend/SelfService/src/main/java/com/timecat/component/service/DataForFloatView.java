package com.timecat.component.service;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/28
 * @description null
 * @usage null
 */
public class DataForFloatView {
    public String icon;
    public String name;
    public String id;

    public DataForFloatView(String icon, String name, String id) {
        this.icon = icon;
        this.name = name;
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
