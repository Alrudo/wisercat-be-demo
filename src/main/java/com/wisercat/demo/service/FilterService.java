package com.wisercat.demo.service;

import com.wisercat.demo.dto.FilterDTO;
import com.wisercat.demo.entity.CriterionEntity;
import com.wisercat.demo.entity.FilterEntity;
import com.wisercat.demo.exception.FilterNotFoundException;
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
        criterionService.criteriaAreValid(filterEntity.getCriteria());
        return filterRepository.save(filterEntity);
    }

    public FilterEntity updateFilter(FilterEntity newFilter) {
        var existingFilter = filterRepository.findById(newFilter.getId())
                .orElseThrow(() -> new FilterNotFoundException(newFilter.getId()));
        criterionService.deleteExcessiveCriteria(existingFilter, newFilter.getCriteria());
        criterionService.criteriaAreValid(newFilter.getCriteria());

        existingFilter.setName(newFilter.getName());
        existingFilter.setSelection(newFilter.getSelection());
        existingFilter.setCriteria(newFilter.getCriteria());
        return filterRepository.save(existingFilter);
    }

    public void deleteFilter(Long id) {
        if (!filterRepository.existsById(id)) {
            throw new FilterNotFoundException(id);
        }
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
}
