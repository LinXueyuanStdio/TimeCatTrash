package com.timecat.component.data.model.habit;

import androidx.annotation.IntDef;

import com.timecat.component.data.model.StringUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A Checkmark represents the completion status of the habit for a given day.
 * Checkmark 表示一天的完成情况
 * <p>
 * While repetitions simply record that the habit was performed at a given date,
 * a checkmark provides more information, such as whether a repetition was
 * expected at that day or not.
 * repetitions 只记录在给定日期习惯的打卡要求，checkmark 可以知道更多，如在某一天是否要求 repetition 打卡
 * <p>
 * Checkmarks are computed automatically from the list of repetitions.
 * Checkmarks 自动从 repetitions 列表里计算出来
 */
public final class Checkmark {
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
    /**
     * Indicates that there was a repetition at the timestamp.
     * 有 repetition 打卡要求且已打卡
     */
    public static final int CHECKED_EXPLICITLY = 2;

    /**
     * Indicates that there was no repetition at the timestamp, but one was not
     * expected in any case, due to the frequency of the habit.
     * 没有 repetition 打卡要求，但由于习惯的频率设置，视为合法，自动进行假打卡
     */
    public static final int CHECKED_IMPLICITLY = 1;

    /**
     * Indicates that there was no repetition at the timestamp, even though a
     * repetition was expected.
     * 有 repetition 打卡要求但是没有打卡
     */
    public static final int UNCHECKED = 0;

    private final Timestamp timestamp;

    /**
     * The value of the checkmark.
     * <p>
     * For boolean habits, this equals either UNCHECKED, CHECKED_EXPLICITLY,
     * or CHECKED_IMPLICITLY.
     * <p>
     * For numerical habits, this number is stored in thousandths. That
     * is, if the user enters value 1.50 on the app, it is stored as 1500.
     */
    private final int value;

    public Checkmark(Timestamp timestamp, int value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Checkmark checkmark = (Checkmark) o;

        return new EqualsBuilder()
                .append(timestamp, checkmark.timestamp)
                .append(value, checkmark.value)
                .isEquals();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(timestamp)
                .append(value)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, StringUtils.defaultToStringStyle())
                .append("timestamp", timestamp)
                .append("value", value)
                .toString();
    }
}
