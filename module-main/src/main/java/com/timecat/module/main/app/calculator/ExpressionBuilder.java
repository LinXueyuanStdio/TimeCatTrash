package com.timecat.module.main.app.calculator;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExpressionBuilder {
    public static final String PROPERTY_UNARY_HIGH_PRECEDENCE = "exp4j.unary.precedence.high";
    private final Map<String, CustomOperator> builtInOperators;
    private final Map<String, CustomFunction> customFunctions;
    private Map<String, CustomOperator> customOperators = new HashMap();
    private String expression;
    private final boolean highUnaryPrecedence;
    private final List<Character> validOperatorSymbols;
    private final Map<String, Double> variables = new LinkedHashMap();

    public ExpressionBuilder(String expression) {
        if (expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression can not be empty!.");
        }
        this.expression = expression;
        boolean z = System.getProperty(PROPERTY_UNARY_HIGH_PRECEDENCE) == null || !System.getProperty(PROPERTY_UNARY_HIGH_PRECEDENCE).equals("false");
        this.highUnaryPrecedence = z;
        this.customFunctions = getBuiltinFunctions();
        this.builtInOperators = getBuiltinOperators();
        this.validOperatorSymbols = getValidOperators();
    }

    private List<Character> getValidOperators() {
        return Arrays.asList(new Character[]{Character.valueOf('!'), Character.valueOf('#'), Character.valueOf('§'), Character.valueOf('$'), Character.valueOf('&'), Character.valueOf(';'), Character.valueOf(':'), Character.valueOf('~'), Character.valueOf('<'), Character.valueOf('>'), Character.valueOf('|'), Character.valueOf('=')});
    }

    private Map<String, CustomOperator> getBuiltinOperators() {
        CustomOperator add = new CustomOperator("+") {
            protected double applyOperation(double[] values) {
                return values[0] + values[1];
            }
        };
        CustomOperator sub = new CustomOperator("-") {
            protected double applyOperation(double[] values) {
                return values[0] - values[1];
            }
        };
        CustomOperator div = new CustomOperator("/", 3) {
            protected double applyOperation(double[] values) {
                if (values[1] != 0.0d) {
                    return values[0] / values[1];
                }
                throw new ArithmeticException("Division by zero!");
            }
        };
        CustomOperator mul = new CustomOperator("*", 3) {
            protected double applyOperation(double[] values) {
                return values[0] * values[1];
            }
        };
        CustomOperator mod = new CustomOperator("%", true, 3) {
            protected double applyOperation(double[] values) {
                if (values[1] != 0.0d) {
                    return values[0] % values[1];
                }
                throw new ArithmeticException("Division by zero!");
            }
        };
        CustomOperator umin = new CustomOperator("'", false, this.highUnaryPrecedence ? 7 : 5, 1) {
            protected double applyOperation(double[] values) {
                return -values[0];
            }
        };
        CustomOperator pow = new CustomOperator("^", false, 5, 2) {
            protected double applyOperation(double[] values) {
                return Math.pow(values[0], values[1]);
            }
        };
        Map<String, CustomOperator> operations = new HashMap();
        operations.put("+", add);
        operations.put("-", sub);
        operations.put("*", mul);
        operations.put("/", div);
        operations.put("'", umin);
        operations.put("^", pow);
        operations.put("%", mod);
        return operations;
    }

    private Map<String, CustomFunction> getBuiltinFunctions() {
        try {
            CustomFunction abs = new CustomFunction("abs") {
                public double applyFunction(double... args) {
                    return Math.abs(args[0]);
                }
            };
            CustomFunction acos = new CustomFunction("acos") {
                public double applyFunction(double... args) {
                    return Math.acos(args[0]);
                }
            };
            CustomFunction asin = new CustomFunction("asin") {
                public double applyFunction(double... args) {
                    return Math.asin(args[0]);
                }
            };
            CustomFunction atan = new CustomFunction("atan") {
                public double applyFunction(double... args) {
                    return Math.atan(args[0]);
                }
            };
            CustomFunction cbrt = new CustomFunction("cbrt") {
                public double applyFunction(double... args) {
                    return Math.cbrt(args[0]);
                }
            };
            CustomFunction ceil = new CustomFunction("ceil") {
                public double applyFunction(double... args) {
                    return Math.ceil(args[0]);
                }
            };
            CustomFunction cos = new CustomFunction("cos") {
                public double applyFunction(double... args) {
                    return Math.cos(args[0]);
                }
            };
            CustomFunction cosh = new CustomFunction("cosh") {
                public double applyFunction(double... args) {
                    return Math.cosh(args[0]);
                }
            };
            CustomFunction exp = new CustomFunction("exp") {
                public double applyFunction(double... args) {
                    return Math.exp(args[0]);
                }
            };
            CustomFunction expm1 = new CustomFunction("expm1") {
                public double applyFunction(double... args) {
                    return Math.expm1(args[0]);
                }
            };
            CustomFunction floor = new CustomFunction("floor") {
                public double applyFunction(double... args) {
                    return Math.floor(args[0]);
                }
            };
            CustomFunction anonymousClass19 = new CustomFunction("log") {
                public double applyFunction(double... args) {
                    return Math.log(args[0]);
                }
            };
            anonymousClass19 = new CustomFunction("sin") {
                public double applyFunction(double... args) {
                    return Math.sin(args[0]);
                }
            };
            anonymousClass19 = new CustomFunction("sinh") {
                public double applyFunction(double... args) {
                    return Math.sinh(args[0]);
                }
            };
            anonymousClass19 = new CustomFunction("sqrt") {
                public double applyFunction(double... args) {
                    return Math.sqrt(args[0]);
                }
            };
            anonymousClass19 = new CustomFunction("tan") {
                public double applyFunction(double... args) {
                    return Math.tan(args[0]);
                }
            };
            anonymousClass19 = new CustomFunction("tanh") {
                public double applyFunction(double... args) {
                    return Math.tanh(args[0]);
                }
            };
            Map<String, CustomFunction> customFunctions = new HashMap();
            customFunctions.put("abs", abs);
            customFunctions.put("acos", acos);
            customFunctions.put("asin", asin);
            customFunctions.put("atan", atan);
            customFunctions.put("cbrt", cbrt);
            customFunctions.put("ceil", ceil);
            customFunctions.put("cos", cos);
            customFunctions.put("cosh", cosh);
            customFunctions.put("exp", exp);
            customFunctions.put("expm1", expm1);
            customFunctions.put("floor", floor);
            customFunctions.put("log", anonymousClass19);
            customFunctions.put("sin", anonymousClass19);
            customFunctions.put("sinh", anonymousClass19);
            customFunctions.put("sqrt", anonymousClass19);
            customFunctions.put("tan", anonymousClass19);
            customFunctions.put("tanh", anonymousClass19);
            return customFunctions;
        } catch (InvalidCustomFunctionException e) {
            throw new RuntimeException(e);
        }
    }

    public Calculable build() throws UnknownFunctionException, UnparsableExpressionException {
        for (CustomOperator op : this.customOperators.values()) {
            int i = 0;
            while (i < op.symbol.length()) {
                if (this.validOperatorSymbols.contains(Character.valueOf(op.symbol.charAt(i)))) {
                    i++;
                } else {
                    throw new UnparsableExpressionException("" + op.symbol + " is not a valid symbol for an operator please choose from: !,#,§,$,&,;,:,~,<,>,|,=");
                }
            }
        }
        for (String varName : this.variables.keySet()) {
            checkVariableName(varName);
            if (this.customFunctions.containsKey(varName)) {
                throw new UnparsableExpressionException("Variable '" + varName + "' cannot have the same name as a function");
            }
        }
        this.builtInOperators.putAll(this.customOperators);
        return RPNConverter.toRPNExpression(this.expression, this.variables, this.customFunctions, this.builtInOperators);
    }

    private void checkVariableName(String varName) throws UnparsableExpressionException {
        char[] name = varName.toCharArray();
        int i = 0;
        while (i < name.length) {
            if (i == 0) {
                if (!(Character.isLetter(name[i]) || name[i] == '_')) {
                    throw new UnparsableExpressionException(varName + " is not a valid variable name: character '" + name[i] + " at " + i);
                }
            } else if (!(Character.isLetter(name[i]) || Character.isDigit(name[i]) || name[i] == '_')) {
                throw new UnparsableExpressionException(varName + " is not a valid variable name: character '" + name[i] + " at " + i);
            }
            i++;
        }
    }

    public ExpressionBuilder withCustomFunction(CustomFunction function) {
        this.customFunctions.put(function.name, function);
        return this;
    }

    public ExpressionBuilder withCustomFunctions(Collection<CustomFunction> functions) {
        for (CustomFunction f : functions) {
            withCustomFunction(f);
        }
        return this;
    }

    public ExpressionBuilder withVariable(String variableName, double value) {
        this.variables.put(variableName, Double.valueOf(value));
        return this;
    }

    public ExpressionBuilder withVariableNames(String... variableNames) {
        for (String variable : variableNames) {
            this.variables.put(variable, null);
        }
        return this;
    }

    public ExpressionBuilder withVariables(Map<String, Double> variableMap) {
        for (Entry<String, Double> v : variableMap.entrySet()) {
            this.variables.put(v.getKey(), v.getValue());
        }
        return this;
    }

    public ExpressionBuilder withOperation(CustomOperator operation) {
        this.customOperators.put(operation.symbol, operation);
        return this;
    }

    public ExpressionBuilder withOperations(Collection<CustomOperator> operations) {
        for (CustomOperator op : operations) {
            withOperation(op);
        }
        return this;
    }

    public ExpressionBuilder withExpression(String expression) {
        this.expression = expression;
        return this;
    }
}
