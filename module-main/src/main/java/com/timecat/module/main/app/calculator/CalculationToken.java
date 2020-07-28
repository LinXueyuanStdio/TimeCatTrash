package com.timecat.module.main.app.calculator;

import java.util.Map;
import java.util.Stack;

abstract class CalculationToken extends Token {
    abstract void mutateStackForCalculation(Stack<Double> stack, Map<String, Double> map);

    CalculationToken(String value) {
        super(value);
    }
}
