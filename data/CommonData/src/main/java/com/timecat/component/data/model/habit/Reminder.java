package com.timecat.component.data.model.habit;

import com.timecat.component.data.model.StringUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class Reminder {
    private final int habitType;

    private final int timesEveryT;
    private final long nextTime;

    public Reminder(int habitType, int timesEveryT, long nextTime) {
        this.habitType = habitType;
        this.timesEveryT = timesEveryT;
        this.nextTime = nextTime;
    }

    public int getHabitType() {
        return habitType;
    }

    public int getTimesEveryT() {
        return timesEveryT;
    }

    public long getNextTime() {
        return nextTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Reminder reminder = (Reminder) o;

        return new EqualsBuilder()
                .append(habitType, reminder.habitType)
                .append(timesEveryT, reminder.timesEveryT)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(habitType)
                .append(timesEveryT)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, StringUtils.defaultToStringStyle())
                .append("habitType", habitType)
                .append("timesEveryT", timesEveryT)
                .toString();
    }
}
