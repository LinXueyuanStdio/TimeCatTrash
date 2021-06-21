package com.timecat.component.greendao.model;

import android.graphics.Color;

import androidx.annotation.Keep;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-23
 * @description null
 * @usage null
 */
@Entity
@Keep
public class TimeBlockTemplate {
    @Id(autoincrement = true)
    Long id = null;
    String title;
    @Index
    long created_datetime = System.currentTimeMillis();
    String color = "#6EADFC";
    boolean archive = false;
    boolean star = false;

    //region
    public void setColorInt(int color) {
        this.color = String.format("#%06X", (0xFFFFFF & color));
    }

    public int getColorInt() {
        return Color.parseColor(color);
    }
    //endregion

    @Generated(hash = 1050783956)
    public TimeBlockTemplate(Long id, String title, long created_datetime,
            String color, boolean archive, boolean star) {
        this.id = id;
        this.title = title;
        this.created_datetime = created_datetime;
        this.color = color;
        this.archive = archive;
        this.star = star;
    }

    @Generated(hash = 1428639103)
    public TimeBlockTemplate() {
    }

    public TimeBlockTemplate(String title) {
        this();
        this.title = title;
    }

    public TimeBlockTemplate(String title, String color) {
        this(title);
        this.color = color;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreated_datetime() {
        return this.created_datetime;
    }

    public void setCreated_datetime(long created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getArchive() {
        return this.archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public boolean getStar() {
        return this.star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
