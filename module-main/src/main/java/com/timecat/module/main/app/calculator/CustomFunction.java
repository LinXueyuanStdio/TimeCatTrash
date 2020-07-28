package com.timecat.module.main.app.calculator;

public abstract class CustomFunction {
    final int argc;
    final String name;

    public abstract double applyFunction(double... dArr);

    protected CustomFunction(String name) throws InvalidCustomFunctionException {
        this.argc = 1;
        this.name = name;
        int firstChar = name.charAt(0);
        if (firstChar >= 65 && firstChar <= 90) {
            return;
        }
        if (firstChar < 97 || firstChar > 122) {
            throw new InvalidCustomFunctionException("functions have to start with a lowercase or uppercase character");
        }
    }

    protected CustomFunction(String name, int argumentCount) throws InvalidCustomFunctionException {
        this.argc = argumentCount;
        this.name = name;
    }

    public int getArgumentCount() {
        return this.argc;
    }
}
