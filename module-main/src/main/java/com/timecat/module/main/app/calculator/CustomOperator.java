package com.timecat.module.main.app.calculator;

public abstract class CustomOperator {
    final boolean leftAssociative;
    final int operandCount;
    final int precedence;
    final String symbol;

    protected abstract double applyOperation(double[] dArr);

    protected CustomOperator(String symbol, boolean leftAssociative, int precedence) {
        this.leftAssociative = leftAssociative;
        this.symbol = symbol;
        this.precedence = precedence;
        this.operandCount = 2;
    }

    protected CustomOperator(String symbol, boolean leftAssociative, int precedence, int operandCount) {
        int i = 1;
        this.leftAssociative = leftAssociative;
        this.symbol = symbol;
        this.precedence = precedence;
        if (operandCount != 1) {
            i = 2;
        }
        this.operandCount = i;
    }

    protected CustomOperator(String symbol) {
        this.leftAssociative = true;
        this.symbol = symbol;
        this.precedence = 1;
        this.operandCount = 2;
    }

    protected CustomOperator(String symbol, int precedence) {
        this.leftAssociative = true;
        this.symbol = symbol;
        this.precedence = precedence;
        this.operandCount = 2;
    }
}
