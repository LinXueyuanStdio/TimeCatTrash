package com.timecat.module.main.miniapp.models;

import android.graphics.drawable.Drawable;

public class ListItemModel {
    int icon;
    Drawable image;
    String metadata;
    String subtitle;
    String title;

    public ListItemModel(int icon, String title, String subtitle) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.metadata = "";
        this.image = null;
    }

    public ListItemModel(Drawable image, String title, String subtitle) {
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.metadata = "";
        this.icon = 0;
    }

    public ListItemModel(int icon, String title, String subtitle, String metadata) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.metadata = metadata;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Drawable getImage() {
        return this.image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
