package com.timecat.data.room.habit;

import android.database.Cursor;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.JSONObject;
import com.timecat.identity.data.base.IJson;

import org.jetbrains.annotations.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-05
 * @description 与习惯一一对应，notifyTime 是下一次的提醒时间
 * @usage null
 */
@Entity(tableName = "HabitReminder", indices = {@Index("id")})
public class HabitReminder implements IJson {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long habitId;
    private long notifyTime;

    public HabitReminder() {
    }

    @Ignore
    public HabitReminder(long id, long habitId, long notifyTime) {
        this.id = id;
        this.habitId = habitId;
        this.notifyTime = notifyTime;
    }

    @Ignore
    public HabitReminder(Cursor c) {
        this(c.getLong(0), c.getLong(1), c.getLong(2));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHabitId() {
        return habitId;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public long getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(long notifyTime) {
        this.notifyTime = notifyTime;
    }


    @NotNull
    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("habitId", habitId);
        jsonObject.put("notifyTime", notifyTime);
        return jsonObject;
    }

    @NotNull
    @Override
    public String toJson() {
        return toJsonObject().toJSONString();
    }

    public static HabitReminder fromJson(String jsonStr) {
        return JSONObject.parseObject(jsonStr, HabitReminder.class);
    }
}
