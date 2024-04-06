package com.wisercat.demo.service;

import com.wisercat.demo.entity.CriterionEntity;
import com.wisercat.demo.entity.FilterEntity;
import com.wisercat.demo.exception.InvalidCriterionException;
import com.wisercat.demo.repository.CriterionRepository;
import com.wisercat.demo.util.CriterionParam;
import com.wisercat.demo.util.CriterionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CriterionService {
    private final CriterionRepository criterionRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void deleteExcessiveCriteria(FilterEntity existingFilter,
                                        List<CriterionEntity> newCriteria) {
        existingFilter.getCriteria().stream()
                .map(CriterionEntity::getId)
                .filter(id -> newCriteria.stream().noneMatch(criterion -> Objects.equals(criterion.getId(), id)))
                .toList()
                .forEach(this::deleteCriterion);
    }

    public void deleteCriterion(Long id) {
        criterionRepository.deleteById(id);
    }

    public void criteriaAreValid(List<CriterionEntity> criteria) {
        if (criteria.size() < 1) {
            throw new InvalidCriterionException();
        }
        for (CriterionEntity criterion : criteria) {
            var type = CriterionType.getType(criterion.getType());
            if (type == null || !CriterionParam.valid(criterion.getParam(), type)) {
                throw new InvalidCriterionException();
            }

            try {
                switch (type) {
                    case AMOUNT -> Integer.parseInt(criterion.getValue());
                    case DATE -> LocalDate.parse(criterion.getValue(), FORMATTER);
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                throw new InvalidCriterionException();
            }
        }
    }
}
