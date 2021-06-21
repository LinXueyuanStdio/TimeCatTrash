package com.timecat.data.room.habit;

import android.database.Cursor;

import androidx.annotation.IntDef;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.JSONObject;
import com.timecat.identity.data.base.IJson;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-05
 * @description 习惯打卡记录
 * @usage null
 */
@Entity(tableName = "HabitRecord", indices = {@Index("id")})
public class HabitRecord implements IJson {
    public static final int TYPE_FINISHED = 0;
    public static final int TYPE_CANCEL_FINISHED = 1;
    public static final int TYPE_FAKE_FINISHED = 2;
    public static final int TYPE_FAKE_CANCEL_FINISHED = 3;

    @IntDef(value = {
            TYPE_FINISHED,//打卡
            TYPE_CANCEL_FINISHED,//取消打卡
            TYPE_FAKE_FINISHED,//自动假打卡
            TYPE_FAKE_CANCEL_FINISHED //自动取消假打卡
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long habitId;//-> Habit
    private long habitReminderId; //-> HabitReminder(id, habitId, notifyTime)
    private long recordTime;
    private int recordYear;
    private int recordMonth;
    private int recordWeek;
    private int recordDay;
    @Type
    private int type;

    public HabitRecord() {
    }

    @Ignore
    public HabitRecord(long habitId, long habitReminderId) {
        this(habitId, habitReminderId, System.currentTimeMillis());
    }

    @Ignore
    public HabitRecord(long habitId, long habitReminderId, long recordTime) {
        this.id = 0;
        this.habitId = habitId;
        this.habitReminderId = habitReminderId;
        this.recordTime = recordTime;
        DateTime dt = new DateTime(recordTime);
        this.recordYear = dt.getYear();
        this.recordMonth = dt.getMonthOfYear();
        this.recordWeek = dt.getWeekOfWeekyear();
        this.recordDay = dt.getDayOfMonth();
        this.type = TYPE_FINISHED;
    }

    @Ignore
    public HabitRecord(long id, long habitId, long habitReminderId, long recordTime,
                       int recordYear, int recordMonth, int recordWeek, int recordDay, @Type int type) {
        this.id = id;
        this.habitId = habitId;
        this.habitReminderId = habitReminderId;
        this.recordTime = recordTime;
        this.recordYear = recordYear;
        this.recordMonth = recordMonth;
        this.recordWeek = recordWeek;
        this.recordDay = recordDay;
        this.type = type;
    }

    @Ignore
    public HabitRecord(Cursor c) {
        this(c.getLong(0), c.getLong(1), c.getLong(2), c.getLong(3),
                c.getInt(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getInt(8));
    }

    @Ignore
    public HabitRecord(HabitRecord c) {
        this(c.id,
                c.habitId,
                c.habitReminderId,
                c.recordTime,
                c.recordYear,
                c.recordMonth,
                c.recordWeek,
                c.recordDay, c.getType());
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

    public long getHabitReminderId() {
        return habitReminderId;
    }

    public void setHabitReminderId(long habitReminderId) {
        this.habitReminderId = habitReminderId;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public int getRecordYear() {
        return recordYear;
    }

    public void setRecordYear(int recordYear) {
        this.recordYear = recordYear;
    }

    public int getRecordMonth() {
        return recordMonth;
    }

    public void setRecordMonth(int recordMonth) {
        this.recordMonth = recordMonth;
    }

    public int getRecordWeek() {
        return recordWeek;
    }

    public void setRecordWeek(int recordWeek) {
        this.recordWeek = recordWeek;
    }

    public int getRecordDay() {
        return recordDay;
    }

    public void setRecordDay(int recordDay) {
        this.recordDay = recordDay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @NotNull
    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("habitId", habitId);
        jsonObject.put("habitReminderId", habitReminderId);
        jsonObject.put("recordTime", recordTime);
        jsonObject.put("recordYear", recordYear);
        jsonObject.put("recordMonth", recordMonth);
        jsonObject.put("recordWeek", recordWeek);
        jsonObject.put("recordDay", recordDay);
        return jsonObject;
    }

    @NotNull
    @Override
    public String toJson() {
        return toJsonObject().toJSONString();
    }

    public static HabitRecord fromJson(String jsonStr) {
        return JSONObject.parseObject(jsonStr, HabitRecord.class);
    }
}
