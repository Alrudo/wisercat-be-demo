package com.wisercat.demo.dto;

import java.util.List;

public record FilterDTO(
        String name,
        Integer selection,
        List<CriterionDTO> criteria
) {
}
