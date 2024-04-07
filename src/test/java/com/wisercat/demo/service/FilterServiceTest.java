package com.wisercat.demo.service;

import com.wisercat.demo.dto.CriterionDTO;
import com.wisercat.demo.dto.FilterDTO;
import com.wisercat.demo.entity.CriterionEntity;
import com.wisercat.demo.entity.FilterEntity;
import com.wisercat.demo.exception.FilterNotFoundException;
import com.wisercat.demo.exception.InvalidCriterionException;
import com.wisercat.demo.exception.InvalidFilterException;
import com.wisercat.demo.repository.FilterRepository;
import com.wisercat.demo.util.CriterionParam;
import com.wisercat.demo.util.CriterionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {
    @Mock
    private FilterRepository filterRepository;
    @Mock
    private CriterionService criterionService;
    @InjectMocks
    private FilterService filterService;

    @Test
    void whenFilterIsValid_createFilterShouldSaveIt() {
        var filterDTO = new FilterDTO("My filter", 1,
                List.of(new CriterionDTO("title", "startsWith", "Meow")));
        filterService.createFilter(filterDTO);
        verify(filterRepository, times(1)).save(any());
    }

    @ParameterizedTest
    @CsvSource({"Valid name, 2", "Valid name, -1234", "'', 1"})
    void whenFilterDataIsInvalid_createFilterShouldThrow(String name, int selection) {
        var filterDTO = new FilterDTO(name, selection,
                List.of(new CriterionDTO("title", "startsWith", "Meow")));
        assertThatThrownBy(() -> filterService.createFilter(filterDTO))
                .isInstanceOf(InvalidFilterException.class);
    }

    @Test
    void whenFilterCriteriaIsInvalid_createFilterShouldThrow() {
        doThrow(new InvalidCriterionException())
                .when(criterionService).criteriaAreValid(any());

        var filterDTO = new FilterDTO("My filter", 1,
                List.of(new CriterionDTO("title", "", "Meow")));

        assertThatThrownBy(() -> filterService.createFilter(filterDTO))
                .isInstanceOf(InvalidCriterionException.class);
    }

    @Test
    void whenFilterWithIdIsNotExisting_updateFilterShouldThrow() {
        when(filterRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        var criterion = CriterionEntity.builder()
                .id(1L)
                .type(CriterionType.AMOUNT.toString().toLowerCase())
                .param(CriterionParam.EQUAL.getValue())
                .value("2")
                .build();

        var filter = FilterEntity.builder()
                .id(1L)
                .name("Filter name")
                .criteria(List.of(criterion))
                .selection(1)
                .build();

        assertThatThrownBy(() -> filterService.updateFilter(filter))
                .isInstanceOf(FilterNotFoundException.class);
    }

    @ParameterizedTest
    @CsvSource({"Valid name, 2", "Valid name, -1", "'', 1"})
    void whenFilterDataIsInvalid_updateFilterShouldThrow(String name, int selection) {
        var criterion = CriterionEntity.builder()
                .id(1L)
                .type(CriterionType.AMOUNT.toString().toLowerCase())
                .param(CriterionParam.EQUAL.getValue())
                .value("2")
                .build();
        var filter = FilterEntity.builder()
                .id(1L)
                .name(name)
                .selection(selection)
                .criteria(List.of(criterion))
                .build();

        assertThatThrownBy(() -> filterService.updateFilter(filter))
                .isInstanceOf(InvalidFilterException.class);
    }

    @Test
    void whenFilterIsValid_updateFilterShouldUpdateIt() {
        var oldCriteria = List.of(
                CriterionEntity.builder()
                        .id(1L)
                        .type(CriterionType.AMOUNT.toString().toLowerCase())
                        .param(CriterionParam.EQUAL.getValue())
                        .value("2")
                        .build(),
                CriterionEntity.builder()
                        .id(2L)
                        .type(CriterionType.TITLE.toString().toLowerCase())
                        .param(CriterionParam.STARTS_WITH.getValue())
                        .value("Meow")
                        .build());

        var newCriteria = List.of(
                CriterionEntity.builder()
                        .id(1L)
                        .type(CriterionType.TITLE.toString().toLowerCase())
                        .param(CriterionParam.STARTS_WITH.getValue())
                        .value("Meow")
                        .build(),
                CriterionEntity.builder()
                        .id(2L)
                        .type(CriterionType.AMOUNT.toString().toLowerCase())
                        .param(CriterionParam.EQUAL.getValue())
                        .value("2")
                        .build());

        var oldFilter = FilterEntity.builder()
                .id(1L)
                .name("Old name")
                .criteria(oldCriteria)
                .selection(1)
                .build();

        var newFilter = FilterEntity.builder()
                .id(1L)
                .name("New name")
                .criteria(newCriteria)
                .selection(2)
                .build();

        when(filterRepository.findById(anyLong()))
                .thenReturn(Optional.of(oldFilter));

        filterService.updateFilter(newFilter);

        verify(filterRepository, times(1)).save(any());
        assertThat(oldFilter.getName()).isEqualTo(newFilter.getName());
        assertThat(oldFilter.getSelection()).isEqualTo(newFilter.getSelection());
        assertThat(oldFilter.getCriteria()).isEqualTo(newFilter.getCriteria());
    }
}
