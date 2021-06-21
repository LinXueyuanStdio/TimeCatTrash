package com.timecat.data.room.habit;

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
 * @description 用户的实际打卡记录 仅已打卡
 * 我们会从这里计算出用户实际看到的打卡 checkmark，根据习惯频率自动假打卡
 * @usage null
 */
@Entity(tableName = "RoomRepetition", indices = {@Index("id")})
public class RoomRepetition implements IJson {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public long recordId;

    public long timestamp;

    public int value;

    public RoomRepetition() {
    }

    @Ignore
    public RoomRepetition(long recordId, long timestamp, int value) {
        this(0, recordId, timestamp, value);
    }

    @Ignore
    public RoomRepetition(int id, long recordId, long timestamp, int value) {
        this.id = id;
        this.recordId = recordId;
        this.timestamp = timestamp;
        this.value = value;
    }

    @NotNull
    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("recordId", recordId);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("value", value);
        return jsonObject;
    }

    @NotNull
    @Override
    public String toJson() {
        return toJsonObject().toJSONString();
    }

    public static RoomRepetition fromJson(String jsonStr) {
        return JSONObject.parseObject(jsonStr, RoomRepetition.class);
    }
}
