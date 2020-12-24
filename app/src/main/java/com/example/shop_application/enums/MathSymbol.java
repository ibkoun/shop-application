package com.example.shop_application.enums;

public enum MathSymbol {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/");

    private String operator;

    MathSymbol(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}
