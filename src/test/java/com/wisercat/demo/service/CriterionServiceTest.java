package com.wisercat.demo.service;

import com.wisercat.demo.entity.CriterionEntity;
import com.wisercat.demo.exception.InvalidCriterionException;
import com.wisercat.demo.repository.CriterionRepository;
import com.wisercat.demo.util.CriterionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CriterionServiceTest {
    @Mock
    private CriterionRepository criterionRepository;
    @InjectMocks
    private CriterionService criterionService;

    @ParameterizedTest
    @ValueSource(strings = {">", ">=", "<", "<=", "=", "=/="})
    void whenAmountCriteriaAreValid_criteriaAreValidShouldNotThrow(String param) {
        var validCriterionList = List.of(
                CriterionEntity.builder()
                        .type(CriterionType.AMOUNT.toString().toLowerCase())
                        .param(param)
                        .value("1")
                        .build());

        assertThatCode(() -> criterionService.criteriaAreValid(validCriterionList))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"startsWith", "endsWith", "contains", "doesNotContain"})
    void whenTitleCriteriaAreValid_criteriaAreValidShouldNotThrow(String param) {
        var validCriterionList = List.of(
                CriterionEntity.builder()
                        .type(CriterionType.TITLE.toString().toLowerCase())
                        .param(param)
                        .value("Meow")
                        .build());

        assertThatCode(() -> criterionService.criteriaAreValid(validCriterionList))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"from", "after", "until", "before", "on"})
    void whenDateCriteriaAreValid_criteriaAreValidShouldNotThrow(String param) {
        var validCriterionList = List.of(
                CriterionEntity.builder()
                        .type(CriterionType.DATE.toString().toLowerCase())
                        .param(param)
                        .value("1996-02-25")
                        .build());

        assertThatCode(() -> criterionService.criteriaAreValid(validCriterionList))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({"amount, >, assfEfg", "date, on, aae-12-GG"})
    void whenValueIsIncorrectFormat_criteriaAreValidShouldThrow(String type, String param, String value) {
        var invalidCriterionList = List.of(
                CriterionEntity.builder()
                        .type(type)
                        .param(param)
                        .value(value)
                        .build());

        assertThatThrownBy(() -> criterionService.criteriaAreValid(invalidCriterionList))
                .isInstanceOf(InvalidCriterionException.class);
    }

    @ParameterizedTest
    @CsvSource({"amount, >>, 2", "date, ON, 1996-02-25", "title, afgsaar, Meow"})
    void whenTypeIsIncorrect_criteriaAreValidShouldThrow(String type, String param, String value) {
        var invalidCriterionList = List.of(
                CriterionEntity.builder()
                        .type(type)
                        .param(param)
                        .value(value)
                        .build());

        assertThatThrownBy(() -> criterionService.criteriaAreValid(invalidCriterionList))
                .isInstanceOf(InvalidCriterionException.class);
    }

    @Test
    void whenCriterionListIsEmpty_criteriaAreValidShouldThrow() {
        var emptyList = new ArrayList<CriterionEntity>();

        assertThatThrownBy(() -> criterionService.criteriaAreValid(emptyList))
                .isInstanceOf(InvalidCriterionException.class);
    }

    @Test
    void whenNewCriteriaListIsShorter_deleteExcessiveCriteriaShouldCallDeleteExcessiveCriteria() {
        var criterionOne = CriterionEntity.builder().id(1L).build();
        var criterionTwo = CriterionEntity.builder().id(2L).build();
        var criterionThree = CriterionEntity.builder().id(3L).build();
        var criterionFour = CriterionEntity.builder().id(4L).build();

        var oldList = List.of(criterionOne, criterionTwo, criterionThree, criterionFour);
        var newList = List.of(criterionOne);

        criterionService.deleteExcessiveCriteria(oldList, newList);
        verify(criterionRepository, times(3)).deleteById(anyLong());
    }

    @Test
    void whenNewCriteriaListIsSameSize_deleteExcessiveCriteriaShouldNotCallDeleteExcessiveCriteria() {
        var criterionOne = CriterionEntity.builder().id(1L).build();
        var criterionTwo = CriterionEntity.builder().id(2L).build();

        var oldList = List.of(criterionOne, criterionTwo);
        var newList = List.of(criterionOne, criterionTwo);

        criterionService.deleteExcessiveCriteria(oldList, newList);
        verify(criterionRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void whenNewCriteriaListIsBiggerSize_deleteExcessiveCriteriaShouldNotCallDeleteExcessiveCriteria() {
        var criterionOne = CriterionEntity.builder().id(1L).build();
        var criterionTwo = CriterionEntity.builder().id(2L).build();

        var oldList = List.of(criterionOne);
        var newList = List.of(criterionOne, criterionTwo);

        criterionService.deleteExcessiveCriteria(oldList, newList);
        verify(criterionRepository, times(0)).deleteById(anyLong());
    }
}
