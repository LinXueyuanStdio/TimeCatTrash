package com.timecat.data.room.reminder;

import android.database.Cursor;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.JSONObject;
import com.timecat.identity.data.base.IJson;
import com.timecat.identity.data.base.ReminderStatus;

import org.jetbrains.annotations.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-17
 * @description null
 * @usage null
 */
@Entity(tableName = "Reminder", indices = {@Index("id")})
public class Reminder implements IJson {

    /**
     * if you want to change the determined time for goal, you should change it in
     * getType(long, long),
     * ReminderDAO#resetGoal(Reminder),
     */

    public static final int UNDERWAY = 0;
    public static final int REMINDED = 2;
    public static final int EXPIRED = 3;

    public static final int GOAL_DAYS = 28;
    public static final long GOAL_MILLIS = GOAL_DAYS * 24 * 60 * 60 * 1000L;

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long notifyTime;
    public int state;
    public long notifyMillis;
    public long createTime;
    public long updateTime;

    public Reminder() {
    }

    @Ignore
    public Reminder(long id, long notifyTime) {
        this.id = id;
        this.notifyTime = notifyTime;
        initNotifyMinutes();
        long curTime = System.currentTimeMillis();
        this.createTime = curTime;
        this.updateTime = curTime;
    }

    @Ignore
    public Reminder(long id, long notifyTime, int state, long notifyMillis, long createTime, long updateTime) {
        this.id = id;
        this.notifyTime = notifyTime;
        this.state = state;
        this.notifyMillis = notifyMillis;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Ignore
    public Reminder(Cursor c) {
        this(c.getLong(0), c.getLong(1), c.getInt(2),
                c.getLong(3), c.getLong(4), c.getLong(5));
    }

    public void initNotifyMinutes() {
        notifyMillis = notifyTime - System.currentTimeMillis();
        notifyMillis += 10; // make it a whole number, not 1999999 but 2000000
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public ReminderStatus getState() {
        return new ReminderStatus(state);
    }

    public boolean isUnderWay() {
        return state == UNDERWAY;
    }

    public boolean isExpired() {
        return state == EXPIRED;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getNotifyMillis() {
        return notifyMillis;
    }

    public void setNotifyMillis(long notifyMillis) {
        this.notifyMillis = notifyMillis;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @NotNull
    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", notifyTime=" + notifyTime +
                ", state=" + state +
                ", notifyMillis=" + notifyMillis +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }


    @NotNull
    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("notifyTime", notifyTime);
        jsonObject.put("state", state);
        jsonObject.put("notifyMillis", notifyMillis);
        jsonObject.put("createTime", createTime);
        jsonObject.put("updateTime", updateTime);
        return jsonObject;
    }

    @NotNull
    @Override
    public String toJson() {
        return toJsonObject().toJSONString();
    }

    public static Reminder fromJson(String jsonStr) {
        return JSONObject.parseObject(jsonStr, Reminder.class);
    }
}
