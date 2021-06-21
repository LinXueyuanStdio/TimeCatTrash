package com.timecat.component.data.model.habit;


import com.timecat.component.data.model.StringUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents how often is the habit repeated.
 */
public class Frequency {
    private final int numerator;

    private final int denominator;

    /**
     * 习惯类型
     */
    private int type;
    private String detail;

    public Frequency(int numerator, int denominator) {
        if (numerator == denominator) numerator = denominator = 1;

        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Frequency(int type, String detail) {
        this(1,1);
        this.type = type;
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Frequency frequency = (Frequency) o;

        return new EqualsBuilder()
                .append(numerator, frequency.numerator)
                .append(denominator, frequency.denominator)
                .isEquals();
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public int getDenominator() {
        return denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(numerator)
                .append(denominator)
                .toHashCode();
    }

    public double toDouble() {
        return (double) numerator / denominator;
    }

    @NotNull
    @Override
    public String toString() {
        return new ToStringBuilder(this, StringUtils.defaultToStringStyle())
                .append("numerator", numerator)
                .append("denominator", denominator)
                .toString();
    }
}
