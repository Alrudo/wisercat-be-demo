package com.wisercat.demo.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CriterionParam {
    MORE(">", CriterionType.AMOUNT),
    MORE_OR_EQUAL(">=", CriterionType.AMOUNT),
    LESS("<", CriterionType.AMOUNT),
    LESS_OR_EQUAL("<=", CriterionType.AMOUNT),
    EQUAL("=", CriterionType.AMOUNT),
    NOT_EQUAL("=/=", CriterionType.AMOUNT),
    STARTS_WITH("startsWith", CriterionType.TITLE),
    ENDS_WITH("endsWith", CriterionType.TITLE),
    CONTAINS("contains", CriterionType.TITLE),
    DOES_NOT_CONTAIN("doesNotContain", CriterionType.TITLE),
    FROM("from", CriterionType.DATE),
    AFTER("after", CriterionType.DATE),
    UNTIL("until", CriterionType.DATE),
    BEFORE("before", CriterionType.DATE),
    ON("on", CriterionType.DATE);

    private final String value;
    private final CriterionType criterionType;

    public static boolean valid(String value, CriterionType type) {
        for (CriterionParam param : CriterionParam.values()) {
            if (param.value.equals(value) && param.criterionType.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
