package com.timecat.module.main.app.calculator;

import java.util.Locale;

public abstract class ExpressionUtil {
    public static String normalizeNumber(String number, Locale loc) throws UnparsableExpressionException {
        return number.replaceAll("e|E", "*10^");
    }

    public static String normalizeNumber(String number) throws UnparsableExpressionException {
        return normalizeNumber(number, Locale.getDefault());
    }
}
