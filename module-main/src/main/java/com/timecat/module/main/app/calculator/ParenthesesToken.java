package com.timecat.module.main.app.calculator;

import java.util.Stack;

class ParenthesesToken extends Token {
    ParenthesesToken(String value) {
        super(value);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ParenthesesToken) {
            return ((ParenthesesToken) obj).getValue().equals(getValue());
        }
        return false;
    }

    public int hashCode() {
        return getValue().hashCode();
    }

    boolean isOpen() {
        return getValue().equals("(") || getValue().equals("[") || getValue().equals("{");
    }

    void mutateStackForInfixTranslation(Stack<Token> operatorStack, StringBuilder output) {
        if (isOpen()) {
            operatorStack.push(this);
            return;
        }
        while (true) {
            Token next = (Token) operatorStack.peek();
            if ((next instanceof OperatorToken) || (next instanceof FunctionToken) || ((next instanceof ParenthesesToken) && !((ParenthesesToken) next).isOpen())) {
                output.append(((Token) operatorStack.pop()).getValue()).append(" ");
            }
        }
//        operatorStack.pop();
    }
}
