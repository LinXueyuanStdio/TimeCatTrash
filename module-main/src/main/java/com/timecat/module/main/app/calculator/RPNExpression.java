package com.timecat.module.main.app.calculator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

class RPNExpression implements Calculable {
    final String expression;
    final List<Token> tokens;
    final Map<String, Double> variables;

    public RPNExpression(List<Token> tokens, String expression, Map<String, Double> variables) {
        this.tokens = tokens;
        this.expression = expression;
        this.variables = variables;
    }

    public double calculate(double... values) throws IllegalArgumentException {
        if (this.variables.size() == 0 && values != null) {
            throw new IllegalArgumentException("there are no variables to set values");
        } else if (values == null || values.length == this.variables.size()) {
            int i = 0;
            if (this.variables.size() > 0 && values != null) {
                for (Entry<String, Double> entry : this.variables.entrySet()) {
                    int i2 = i + 1;
                    entry.setValue(Double.valueOf(values[i]));
                    i = i2;
                }
            }
            Stack<Double> stack = new Stack();
            for (Token t : this.tokens) {
                ((CalculationToken) t).mutateStackForCalculation(stack, this.variables);
            }
            return ((Double) stack.pop()).doubleValue();
        } else {
            throw new IllegalArgumentException("The are an unequal number of variables and arguments");
        }
    }

    public String getExpression() {
        return this.expression;
    }

    public void setVariable(String name, double value) {
        this.variables.put(name, Double.valueOf(value));
    }

    public double calculate() {
        return calculate(null);
    }
}
