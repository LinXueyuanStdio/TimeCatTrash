package com.timecat.module.main.app.calculator;

import java.util.Map;
import java.util.Stack;

class OperatorToken extends CalculationToken {
    CustomOperator operation;

    OperatorToken(String value, CustomOperator operation) {
        super(value);
        this.operation = operation;
    }

    double applyOperation(double... values) {
        return this.operation.applyOperation(values);
    }

    public boolean equals(Object obj) {
        if (obj instanceof OperatorToken) {
            return ((OperatorToken) obj).getValue().equals(getValue());
        }
        return false;
    }

    public int hashCode() {
        return getValue().hashCode();
    }

    void mutateStackForCalculation(Stack<Double> stack, Map<String, Double> map) {
        double[] operands = new double[this.operation.operandCount];
        for (int i = 0; i < this.operation.operandCount; i++) {
            operands[(this.operation.operandCount - i) - 1] = ((Double) stack.pop()).doubleValue();
        }
        stack.push(Double.valueOf(this.operation.applyOperation(operands)));
    }

    void mutateStackForInfixTranslation(Stack<Token> operatorStack, StringBuilder output) {
        while (!operatorStack.isEmpty()) {
            Token before = (Token) operatorStack.peek();
            if (before == null || (!(before instanceof OperatorToken) && !(before instanceof FunctionToken))) {
                break;
            } else if (before instanceof FunctionToken) {
                operatorStack.pop();
                output.append(before.getValue()).append(" ");
            } else {
                OperatorToken stackOperator = (OperatorToken) before;
                if (!isLeftAssociative() || getPrecedence() > stackOperator.getPrecedence()) {
                    if (isLeftAssociative() || getPrecedence() >= stackOperator.getPrecedence()) {
                        break;
                    }
                    output.append(((Token) operatorStack.pop()).getValue()).append(" ");
                } else {
                    output.append(((Token) operatorStack.pop()).getValue()).append(" ");
                }
            }
        }
        operatorStack.push(this);
    }

    private boolean isLeftAssociative() {
        return this.operation.leftAssociative;
    }

    private int getPrecedence() {
        return this.operation.precedence;
    }
}
