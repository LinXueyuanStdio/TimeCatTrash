package com.timecat.module.main.app.calculator;

import java.util.Map;
import java.util.Stack;

class NumberToken extends CalculationToken {
    private final double doubleValue;

    NumberToken(String value) {
        super(value);
        if (value.indexOf(69) > 0 || value.indexOf(101) > 0) {
            value = value.toLowerCase();
            int pos = value.indexOf(101);
            this.doubleValue = Math.pow(10.0d, Double.parseDouble(value.substring(pos + 1))) * Double.parseDouble(value.substring(0, pos));
            return;
        }
        this.doubleValue = Double.parseDouble(value);
    }

    public boolean equals(Object obj) {
        if (obj instanceof NumberToken) {
            return ((NumberToken) obj).getValue().equals(getValue());
        }
        return false;
    }

    public int hashCode() {
        return getValue().hashCode();
    }

    void mutateStackForCalculation(Stack<Double> stack, Map<String, Double> map) {
        stack.push(Double.valueOf(this.doubleValue));
    }

    void mutateStackForInfixTranslation(Stack<Token> stack, StringBuilder output) {
        output.append(getValue()).append(' ');
    }
}
