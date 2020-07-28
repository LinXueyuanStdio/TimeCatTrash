package com.timecat.module.main.app.calculator;

import java.util.List;
import java.util.Map;
import java.util.Stack;

abstract class RPNConverter {
    RPNConverter() {
    }

    private static String substituteUnaryOperators(String expr, Map<String, CustomOperator> operators) {
        StringBuilder resultBuilder = new StringBuilder();
        int whitespaceCount = 0;
        for (int i = 0; i < expr.length(); i++) {
            boolean afterOperator = false;
            boolean afterParantheses = false;
            boolean expressionStart = false;
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) {
                whitespaceCount++;
                resultBuilder.append(c);
            } else {
                if (resultBuilder.length() == whitespaceCount) {
                    expressionStart = true;
                }
                if (resultBuilder.length() > whitespaceCount) {
                    if (isOperatorCharacter(resultBuilder.charAt((resultBuilder.length() - 1) - whitespaceCount), operators)) {
                        afterOperator = true;
                    } else if (resultBuilder.charAt((resultBuilder.length() - 1) - whitespaceCount) == '(') {
                        afterParantheses = true;
                    }
                }
                switch (c) {
                    case '+':
                        if (!(resultBuilder.length() <= 0 || afterOperator || afterParantheses || expressionStart)) {
                            resultBuilder.append(c);
                            break;
                        }
                    case '-':
                        if (resultBuilder.length() > 0 && !afterOperator && !afterParantheses && !expressionStart) {
                            resultBuilder.append(c);
                            break;
                        }
                        resultBuilder.append('\'');
                        break;
                    default:
                        resultBuilder.append(c);
                        break;
                }
                whitespaceCount = 0;
            }
        }
        return resultBuilder.toString();
    }

    static RPNExpression toRPNExpression(String infix, Map<String, Double> variables, Map<String, CustomFunction> customFunctions, Map<String, CustomOperator> operators) throws UnknownFunctionException, UnparsableExpressionException {
        Tokenizer tokenizer = new Tokenizer(variables.keySet(), customFunctions, operators);
        StringBuilder output = new StringBuilder(infix.length());
        Stack<Token> operatorStack = new Stack();
        List<Token> tokens = tokenizer.getTokens(substituteUnaryOperators(infix, operators));
        validateRPNExpression(tokens, operators);
        for (Token token : tokens) {
            token.mutateStackForInfixTranslation(operatorStack, output);
        }
        while (operatorStack.size() > 0) {
            output.append(((Token) operatorStack.pop()).getValue()).append(" ");
        }
        String postfix = output.toString().trim();
        return new RPNExpression(tokenizer.getTokens(postfix), postfix, variables);
    }

    private static void validateRPNExpression(List<Token> tokens, Map<String, CustomOperator> map) throws UnparsableExpressionException {
        for (int i = 1; i < tokens.size(); i++) {
            Token t = (Token) tokens.get(i);
            if ((tokens.get(i - 1) instanceof NumberToken) && ((t instanceof VariableToken) || (((t instanceof ParenthesesToken) && ((ParenthesesToken) t).isOpen()) || (t instanceof FunctionToken)))) {
                throw new UnparsableExpressionException("Implicit multiplication is not supported. E.g. always use '2*x' instead of '2x'");
            }
        }
    }

    private static boolean isOperatorCharacter(char c, Map<String, CustomOperator> operators) {
        for (String symbol : operators.keySet()) {
            if (symbol.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
}
