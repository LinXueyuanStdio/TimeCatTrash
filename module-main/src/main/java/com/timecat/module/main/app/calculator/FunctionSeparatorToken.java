package com.timecat.module.main.app.calculator;

import java.util.Stack;

class FunctionSeparatorToken extends Token {
    FunctionSeparatorToken() {
        super(",");
    }

    void mutateStackForInfixTranslation(Stack<Token> operatorStack, StringBuilder output) {
        while (true) {
            Token token = (Token) operatorStack.peek();
            if (!(token instanceof ParenthesesToken) && !token.getValue().equals("(")) {
                output.append(((Token) operatorStack.pop()).getValue()).append(" ");
            } else {
                return;
            }
        }
    }
}
