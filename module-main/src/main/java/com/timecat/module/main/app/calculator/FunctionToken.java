package com.timecat.module.main.app.calculator;

import java.util.Map;
import java.util.Stack;

class FunctionToken extends CalculationToken {
    final CustomFunction function;
    final String functionName;

    FunctionToken(String value, CustomFunction function) throws UnknownFunctionException {
        super(value);
        if (value == null) {
            throw new UnknownFunctionException(value);
        }
        try {
            this.functionName = function.name;
            this.function = function;
        } catch (IllegalArgumentException e) {
            throw new UnknownFunctionException(value);
        }
    }

    String getName() {
        return this.functionName;
    }

    public boolean equals(Object obj) {
        if (obj instanceof FunctionToken) {
            return this.functionName.equals(((FunctionToken) obj).functionName);
        }
        return false;
    }

    public int hashCode() {
        return this.functionName.hashCode();
    }

    void mutateStackForCalculation(Stack<Double> stack, Map<String, Double> map) {
        double[] args = new double[this.function.argc];
        for (int i = 0; i < this.function.argc; i++) {
            args[i] = ((Double) stack.pop()).doubleValue();
        }
        stack.push(Double.valueOf(this.function.applyFunction(ArrayUtil.reverse(args))));
    }

    void mutateStackForInfixTranslation(Stack<Token> operatorStack, StringBuilder output) {
        operatorStack.push(this);
    }
}
