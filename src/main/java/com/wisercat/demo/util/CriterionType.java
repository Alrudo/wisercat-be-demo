package com.wisercat.demo.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CriterionType {
    AMOUNT("amount"),
    TITLE("title"),
    DATE("date");

    private final String value;

    public static CriterionType getValue(String value) {
        for (CriterionType type: CriterionType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
