package com.timecat.module.main.app.calculator;

import java.util.Map;
import java.util.Stack;

class VariableToken extends CalculationToken {
    VariableToken(String value) {
        super(value);
    }

    public boolean equals(Object obj) {
        if (obj instanceof VariableToken) {
            return super.getValue().equals(((VariableToken) obj).getValue());
        }
        return false;
    }

    public int hashCode() {
        return super.getValue().hashCode();
    }

    void mutateStackForCalculation(Stack<Double> stack, Map<String, Double> variableValues) {
        stack.push(Double.valueOf(((Double) variableValues.get(getValue())).doubleValue()));
    }

    void mutateStackForInfixTranslation(Stack<Token> stack, StringBuilder output) {
        output.append(getValue()).append(" ");
    }
}
