package com.timecat.module.main.app.calculator;

public class CommandlineInterpreter {
    private static void calculateExpression(String string) {
        try {
            System.out.println(new ExpressionBuilder(string).build().calculate());
        } catch (UnparsableExpressionException e) {
            e.printStackTrace();
        } catch (UnknownFunctionException e2) {
            e2.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
        } else {
            calculateExpression(args[0]);
        }
    }

    private static void printUsage() {
        StringBuilder usage = new StringBuilder();
        usage.append("Commandline Expression Parser\n\n").append("Example: ").append("\n").append("java -jar exp4j.jar \"2.12 * log(23) * (12 - 4)\"\n\n").append("written by fas@congrace.de");
        System.err.println(usage.toString());
    }
}
