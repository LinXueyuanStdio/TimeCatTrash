package com.timecat.module.main.app.calculator;

public interface Calculable {
    double calculate();

    double calculate(double... dArr);

    String getExpression();

    void setVariable(String str, double d);
}
