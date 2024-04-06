package com.wisercat.demo.util;

public enum CriterionType {
    AMOUNT,
    TITLE,
    DATE;

    public static CriterionType getType(String value) {
        try {
            return CriterionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
