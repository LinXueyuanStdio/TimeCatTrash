package com.timecat.component.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/11
 * @description 定一个闹钟只需要这些字段就够了，不用整个DBRoutine
 * @usage null
 */
public class RoutineClock implements Parcelable {

    /**
     * 闹钟id
     */
    private int id;

    /**
     * 小时
     */
    private int hour;

    /**
     * 分钟
     */
    private int minute;

    /**
     * 重复
     */
    private String repeat;

    /**
     * 周期
     */
    private String weeks;

    /**
     * 标签
     */
    private String tag;

    /**
     * 铃声名
     */
    private String ringName;

    /**
     * 铃声地址
     */
    private String ringUrl;

    /**
     * 铃声选择标记界面
     */
    private int ringPager;

    /**
     * 音量
     */
    private int volume;

    /**
     * 振动
     */
    private boolean vibrate;

    /**
     * 督促
     */
    private boolean nap;

    /**
     * 督促间隔
     */
    private int napInterval;

    /**
     * 督促次数
     */
    private int napTimes;

    /**
     * 天气提示
     */
    private boolean weaPrompt;

    /**
     * 开关
     */
    private boolean onOff;

    public RoutineClock() {
        super();
    }

    /**
     * 闹钟实例构造方法
     *
     * @param hour        小时
     * @param minute      分钟
     * @param repeat      重复
     * @param weeks       周期
     * @param tag         标签
     * @param ringName    铃声名
     * @param ringUrl     铃声地址
     * @param ringPager   铃声界面
     * @param volume      音量
     * @param vibrate     振动
     * @param nap         督促
     * @param napInterval 督促间隔
     * @param napTimes    督促次数
     * @param weaPrompt   天气提示
     * @param onOff       开关
     */
    public RoutineClock(int hour, int minute, String repeat,
                        String weeks, String tag, String ringName, String ringUrl,
                        int ringPager, int volume, boolean vibrate, boolean nap,
                        int napInterval, int napTimes, boolean weaPrompt, boolean onOff) {
        super();
        this.hour = hour;
        this.minute = minute;
        this.repeat = repeat;
        this.weeks = weeks;
        this.tag = tag;
        this.ringName = ringName;
        this.ringUrl = ringUrl;
        this.ringPager = ringPager;
        this.volume = volume;
        this.vibrate = vibrate;
        this.nap = nap;
        this.napInterval = napInterval;
        this.napTimes = napTimes;
        this.weaPrompt = weaPrompt;
        this.onOff = onOff;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(hour);
        out.writeInt(minute);
        out.writeString(repeat);
        out.writeString(weeks);
        out.writeString(tag);
        out.writeString(ringName);
        out.writeString(ringUrl);
        out.writeInt(ringPager);
        out.writeInt(volume);
        out.writeByte((byte) (vibrate ? 1 : 0));
        out.writeByte((byte) (nap ? 1 : 0));
        out.writeInt(napInterval);
        out.writeInt(napTimes);
        out.writeByte((byte) (weaPrompt ? 1 : 0));
        out.writeByte((byte) (onOff ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private RoutineClock(Parcel in) {
        id = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        repeat = in.readString();
        weeks = in.readString();
        tag = in.readString();
        ringName = in.readString();
        ringUrl = in.readString();
        ringPager = in.readInt();
        volume = in.readInt();
        vibrate = in.readByte() != 0;
        nap = in.readByte() != 0;
        napInterval = in.readInt();
        napTimes = in.readInt();
        weaPrompt = in.readByte() != 0;
        onOff = in.readByte() != 0;
    }

    public static final Parcelable.Creator<RoutineClock> CREATOR = new Creator<RoutineClock>() {

        @Override
        public RoutineClock createFromParcel(Parcel in) {
            return new RoutineClock(in);
        }

        @Override
        public RoutineClock[] newArray(int size) {

            return new RoutineClock[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getNapInterval() {
        return napInterval;
    }

    public void setNapInterval(int napInterval) {
        this.napInterval = napInterval;
    }

    public int getNapTimes() {
        return napTimes;
    }

    public void setNapTimes(int napTimes) {
        this.napTimes = napTimes;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRingName() {
        return ringName;
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }

    public String getRingUrl() {
        return ringUrl;
    }

    public int getRingPager() {
        return ringPager;
    }

    public void setRingPager(int ringPager) {
        this.ringPager = ringPager;
    }

    public void setRingUrl(String ringUrl) {
        this.ringUrl = ringUrl;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isNap() {
        return nap;
    }

    public void setNap(boolean nap) {
        this.nap = nap;
    }

    public boolean isWeaPrompt() {
        return weaPrompt;
    }

    public void setWeaPrompt(boolean weaPrompt) {
        this.weaPrompt = weaPrompt;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }
}
