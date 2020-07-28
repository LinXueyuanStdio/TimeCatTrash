package com.timecat.module.main.app.calculator;

public class InvalidCustomFunctionException extends Exception {
    private static final long serialVersionUID = 1;

    public InvalidCustomFunctionException(String message) {
        super(message);
    }
}
