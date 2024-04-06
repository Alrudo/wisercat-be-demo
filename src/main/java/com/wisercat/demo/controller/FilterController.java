package com.wisercat.demo.controller;

import com.wisercat.demo.entity.FilterEntity;
import com.wisercat.demo.dto.FilterDTO;
import com.wisercat.demo.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/demo")
public class FilterController {
    private final FilterService filterService;

    @GetMapping
    public ResponseEntity<List<FilterEntity>> getAllFilters() {
        var filters = filterService.getAllFilters();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(filters);
    }

    @PostMapping
    public ResponseEntity<FilterEntity> createFilter(@RequestBody FilterDTO filter) {
        var createdFilter = filterService.createFilter(filter);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdFilter);
    }

    @PutMapping
    public ResponseEntity<FilterEntity> updateFilter(@RequestBody FilterEntity filter) {
        var updatedFilter = filterService.updateFilter(filter);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedFilter);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFilter(@PathVariable Long id) {
        filterService.deleteFilter(id);
        return ResponseEntity.noContent().build();
    }
}
