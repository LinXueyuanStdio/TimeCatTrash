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
public class TimeBlock {
    /**
     * id == null 表示自动自增生成id
     */
    @Id(autoincrement = true)
    Long id = null;
    String title;
    @Index
    long created_datetime = System.currentTimeMillis();
    String color = "#6EADFC";
    long key;

    //region
    public TimeBlock(long key, String title) {
        this();
        this.key = key;
        this.title = title;
    }

    public TimeBlock(long key, String title, String color) {
        this(key, title);
        this.color = color;
    }

    public void setColorInt(int color) {
        this.color = String.format("#%06X", (0xFFFFFF & color));
    }

    public int getColorInt() {
        return Color.parseColor(color);
    }

    /**
     * @param year
     * @param month
     * @param day
     * @param hour  0-23
     * @param min   0, 15, 30, 45
     * @return
     */
    public static long buildKey(int year, int month, int day, int hour, int min) {
        int timeBlockViewKey = buildTimeBlockViewKey(hour, min);
        return buildKey(year, month, day, timeBlockViewKey);
    }

    /**
     * @param year
     * @param month
     * @param day
     * @param timeBlockViewKey 1230 [== 12:30 (12 时 30 分)]
     * @return
     */
    public static long buildKey(int year, int month, int day, int timeBlockViewKey) {
        long KEY = 0;
        KEY += year;
        KEY *= 100 * 100 * 100 * 100;
        KEY += month * 100 * 100 * 100;
        KEY += day * 100 * 100;
        KEY += timeBlockViewKey;
        return KEY;
    }

    /**
     * @param hour 0-23
     * @param min  0, 15, 30, 45
     * @return
     */
    public static int buildTimeBlockViewKey(int hour, int min) {
        return hour * 100 + min;
    }
    //endregion

    @Generated(hash = 1433948672)
    public TimeBlock(Long id, String title, long created_datetime, String color, long key) {
        this.id = id;
        this.title = title;
        this.created_datetime = created_datetime;
        this.color = color;
        this.key = key;
    }

    @Generated(hash = 2025563825)
    public TimeBlock() {
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

    public long getKey() {
        return this.key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
