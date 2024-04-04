package com.wisercat.demo.service;

import com.wisercat.demo.entity.FilterEntity;
import com.wisercat.demo.exception.FilterNotFoundException;
import com.wisercat.demo.repository.FilterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterService {
    private final FilterRepository filterRepository;

    public List<FilterEntity> getAllFilters() {
        return filterRepository.findAll();
    }

    public FilterEntity getFilter(Long id) {
        return filterRepository.findById(id)
                .orElseThrow(() -> new FilterNotFoundException(id));
    }

    public FilterEntity createFilter(FilterEntity filter) {
        return filterRepository.save(filter);
    }

    public FilterEntity updateFilter(Long id, FilterEntity newFilter) {
        var existingFilter = filterRepository.findById(id)
                .orElseThrow(() -> new FilterNotFoundException(id));
        existingFilter.setName(newFilter.getName());
        existingFilter.setSelection(newFilter.getSelection());
        existingFilter.setCriterias(newFilter.getCriterias());
        return filterRepository.save(existingFilter);
    }
}
