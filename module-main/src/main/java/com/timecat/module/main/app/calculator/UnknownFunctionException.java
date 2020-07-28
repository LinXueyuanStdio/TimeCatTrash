package com.timecat.module.main.app.calculator;

public class UnknownFunctionException extends Exception {
    private static final long serialVersionUID = 1;

    public UnknownFunctionException(String functionName) {
        super("Unknown function: " + functionName);
    }
}
