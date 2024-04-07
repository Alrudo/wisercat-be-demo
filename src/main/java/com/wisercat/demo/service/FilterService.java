package com.wisercat.demo.service;

import com.wisercat.demo.dto.FilterDTO;
import com.wisercat.demo.entity.CriterionEntity;
import com.wisercat.demo.entity.FilterEntity;
import com.wisercat.demo.exception.FilterNotFoundException;
import com.wisercat.demo.exception.InvalidFilterException;
import com.wisercat.demo.repository.FilterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FilterService {
    private final FilterRepository filterRepository;
    private final CriterionService criterionService;

    public List<FilterEntity> getAllFilters() {
        return filterRepository.findAll();
    }

    public FilterEntity createFilter(FilterDTO filter) {
        var filterEntity = mapDTOToEntity(filter);
        filterIsValid(filterEntity);
        criterionService.criteriaAreValid(filterEntity.getCriteria());

        return filterRepository.save(filterEntity);
    }

    public FilterEntity updateFilter(FilterEntity newFilter) {
        filterIsValid(newFilter);
        criterionService.criteriaAreValid(newFilter.getCriteria());

        var existingFilter = filterRepository.findById(newFilter.getId())
                .orElseThrow(() -> new FilterNotFoundException(newFilter.getId()));
        criterionService.deleteExcessiveCriteria(existingFilter.getCriteria(), newFilter.getCriteria());

        existingFilter.setName(newFilter.getName());
        existingFilter.setSelection(newFilter.getSelection());
        existingFilter.setCriteria(newFilter.getCriteria());
        existingFilter.getCriteria().forEach(criterion -> criterion.setFilter(existingFilter));
        return filterRepository.save(existingFilter);
    }

    public void deleteFilter(Long id) {
        filterRepository.deleteById(id);
    }

    private FilterEntity mapDTOToEntity(FilterDTO filterDTO) {
        var filterEntity = FilterEntity.builder()
                .name(filterDTO.name())
                .selection(filterDTO.selection())
                .build();

        var criteria = filterDTO.criteria()
                .stream()
                .map(criterion -> CriterionEntity.builder()
                        .type(criterion.type())
                        .param(criterion.param())
                        .value(criterion.value())
                        .filter(filterEntity)
                        .build())
                .toList();
        filterEntity.setCriteria(criteria);
        return filterEntity;
    }

    private void filterIsValid(FilterEntity filter) {
        boolean invalidName = filter.getName().length() == 0 || filter.getName().length() > 255;
        boolean invalidSelection = filter.getSelection() < 1 || filter.getSelection() > filter.getCriteria().size();
        if (invalidName || invalidSelection) {
            throw new InvalidFilterException();
        }
    }
}
