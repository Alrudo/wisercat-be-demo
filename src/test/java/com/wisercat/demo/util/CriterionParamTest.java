package com.wisercat.demo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CriterionParamTest {

    @ParameterizedTest
    @ValueSource(strings = {">", ">=", "<", "<=", "=", "=/="})
    void whenAmountDataIsValid_validShouldReturnTrue(String param) {
        assertThat(CriterionParam.valid(param, CriterionType.AMOUNT)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"startsWith", "endsWith", "contains", "doesNotContain"})
    void whenTitleDataIsValid_validShouldReturnTrue(String param) {
        assertThat(CriterionParam.valid(param, CriterionType.TITLE)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"from", "after", "until", "before", "on"})
    void whenDateDataIsValid_validShouldReturnTrue(String param) {
        assertThat(CriterionParam.valid(param, CriterionType.DATE)).isTrue();
    }

    @Test
    void whenDataIsNotValid_validShouldReturnFalse() {
        assertThat(CriterionParam.valid("asgres", CriterionType.AMOUNT)).isFalse();
        assertThat(CriterionParam.valid("g4211rge", CriterionType.TITLE)).isFalse();
        assertThat(CriterionParam.valid("aag4as", CriterionType.DATE)).isFalse();
    }
}
