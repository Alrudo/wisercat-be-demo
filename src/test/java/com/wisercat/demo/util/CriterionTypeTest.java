package com.wisercat.demo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class CriterionTypeTest {

    @ParameterizedTest
    @CsvSource({"amount, AMOUNT", "title, TITLE", "date, DATE"})
    void whenTypeExists_getTypeShouldReturnCorrectType(String type, CriterionType cType) {
        assertThat(CriterionType.getValue(type)).isEqualTo(cType);
    }

    @Test
    void whenTypeDoesNotExists_getTypeShouldReturnNull() {
        assertThat(CriterionType.getValue("Invalid")).isNull();
    }
}
