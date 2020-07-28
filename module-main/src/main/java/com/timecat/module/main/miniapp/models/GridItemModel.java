package com.timecat.module.main.miniapp.models;

public class GridItemModel {
    private int iconResource;
    private String title;

    public GridItemModel(int iconResource, String title) {
        this.iconResource = iconResource;
        this.title = title;
    }

    public int getIconResource() {
        return this.iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
