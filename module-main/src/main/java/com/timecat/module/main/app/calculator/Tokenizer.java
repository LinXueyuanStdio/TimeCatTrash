package com.timecat.module.main.app.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Tokenizer {
    private final Map<String, CustomFunction> functions;
    private final Map<String, CustomOperator> operators;
    private final Set<String> variableNames;
    int offset;
    Tokenizer(Set<String> variableNames, Map<String, CustomFunction> functions, Map<String, CustomOperator> operators) {
        this.variableNames = variableNames;
        this.functions = functions;
        this.operators = operators;
    }

    private boolean isDigitOrDecimalSeparator(char c) {
        return Character.isDigit(c) || c == '.';
    }

    private boolean isNotationSeparator(char c) {
        return c == 'e' || c == 'E';
    }

    private boolean isVariable(String name) {
        if (this.variableNames != null) {
            for (String var : this.variableNames) {
                if (name.equals(var)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFunction(String name) {
        return this.functions.containsKey(name);
    }

    private boolean isOperatorCharacter(char c) {
        for (String symbol : this.operators.keySet()) {
            if (symbol.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    List<Token> getTokens(String expression) throws UnparsableExpressionException, UnknownFunctionException {
        List<Token> tokens = new ArrayList<>();
        char[] chars = expression.toCharArray();
        int openBraces = 0;
        int openCurly = 0;
        int openSquare = 0;
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (c != ' ') {
                Token lastToken;
                if (Character.isDigit(c)) {
                    StringBuilder valueBuilder = new StringBuilder(1);
                    valueBuilder.append(c);
                    int numberLen = 1;
                    boolean lastCharNotationSeparator = false;
                    boolean notationSeparatorOccured = false;
                    while (chars.length > i + numberLen) {
                        if (!isDigitOrDecimalSeparator(chars[i + numberLen])) {
                            if (!isNotationSeparator(chars[i + numberLen])) {
                                if (!lastCharNotationSeparator || (chars[i + numberLen] != '-' && chars[i + numberLen] != '+')) {
                                    break;
                                }
                                valueBuilder.append(chars[i + numberLen]);
                                lastCharNotationSeparator = false;
                            } else if (notationSeparatorOccured) {
                                throw new UnparsableExpressionException("Number can have only one notation separator 'e/E'");
                            } else {
                                valueBuilder.append(chars[i + numberLen]);
                                lastCharNotationSeparator = true;
                                notationSeparatorOccured = true;
                            }
                        } else {
                            valueBuilder.append(chars[i + numberLen]);
                            lastCharNotationSeparator = false;
                        }
                        numberLen++;
                    }
                    i += numberLen - 1;
                    lastToken = new NumberToken(valueBuilder.toString());
                } else if (Character.isLetter(c) || c == '_') {
                    StringBuilder nameBuilder = new StringBuilder();
                    nameBuilder.append(c);
                    offset = 1;
                    while (chars.length > i + offset && (Character.isLetter(chars[i + offset]) || Character.isDigit(chars[i + offset]) || chars[i + offset] == '_')) {
                        int offset1 = offset + 1;
                        nameBuilder.append(chars[i + offset]);
                        offset = offset1;
                    }
                    String name = nameBuilder.toString();
                    if (isVariable(name)) {
                        i += offset - 1;
                        lastToken = new VariableToken(name);
                    } else if (isFunction(name)) {
                        i += offset - 1;
                        lastToken = new FunctionToken(name, (CustomFunction) this.functions.get(name));
                    } else {
                        throw new UnparsableExpressionException(expression, c, i + 1);
                    }
                } else if (c == ',') {
                    lastToken = new FunctionSeparatorToken();
                } else if (isOperatorCharacter(c)) {
                    StringBuilder symbolBuilder = new StringBuilder();
                    symbolBuilder.append(c);
                    offset = 1;
                    while (chars.length > i + offset && isOperatorCharacter(chars[i + offset]) && isOperatorStart(symbolBuilder.toString() + chars[i + offset])) {
                        symbolBuilder.append(chars[i + offset]);
                        offset++;
                    }
                    String symbol = symbolBuilder.toString();
                    if (this.operators.containsKey(symbol)) {
                        i += offset - 1;
                        lastToken = new OperatorToken(symbol, (CustomOperator) this.operators.get(symbol));
                    } else {
                        throw new UnparsableExpressionException(expression, c, i + 1);
                    }
                } else if (c == '(') {
                    openBraces++;
                    lastToken = new ParenthesesToken(String.valueOf(c));
                } else if (c == '{') {
                    openCurly++;
                    lastToken = new ParenthesesToken(String.valueOf(c));
                } else if (c == '[') {
                    openSquare++;
                    lastToken = new ParenthesesToken(String.valueOf(c));
                } else if (c == ')') {
                    openBraces--;
                    lastToken = new ParenthesesToken(String.valueOf(c));
                } else if (c == '}') {
                    openCurly--;
                    lastToken = new ParenthesesToken(String.valueOf(c));
                } else if (c == ']') {
                    openSquare--;
                    lastToken = new ParenthesesToken(String.valueOf(c));
                } else {
                    throw new UnparsableExpressionException(expression, c, i + 1);
                }
                tokens.add(lastToken);
            }
            i++;
        }
        if (openCurly == 0) {
            int i2;
            int i3;
            if (openBraces != 0) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            if (openSquare != 0) {
                i3 = 1;
            } else {
                i3 = 0;
            }
            if ((i3 | i2) == 0) {
                return tokens;
            }
        }
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append("There are ");
        boolean first = true;
        if (openBraces != 0) {
            errorBuilder.append(Math.abs(openBraces) + " unmatched parantheses ");
            first = false;
        }
        if (openCurly != 0) {
            if (!first) {
                errorBuilder.append(" and ");
            }
            errorBuilder.append(Math.abs(openCurly) + " unmatched curly brackets ");
            first = false;
        }
        if (openSquare != 0) {
            if (!first) {
                errorBuilder.append(" and ");
            }
            errorBuilder.append(Math.abs(openSquare) + " unmatched square brackets ");
        }
        errorBuilder.append("in expression '" + expression + "'");
        throw new UnparsableExpressionException(errorBuilder.toString());
    }

    private boolean isOperatorStart(String op) {
        for (String operatorName : this.operators.keySet()) {
            if (operatorName.startsWith(op)) {
                return true;
            }
        }
        return false;
    }
}
