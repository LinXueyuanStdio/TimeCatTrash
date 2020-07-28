package com.timecat.module.main.app.calculator;

import java.util.Stack;

abstract class Token {
    private final String value;

    abstract void mutateStackForInfixTranslation(Stack<Token> stack, StringBuilder stringBuilder);

    Token(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
}
