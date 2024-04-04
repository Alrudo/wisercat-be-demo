package com.wisercat.demo.controller;

import com.wisercat.demo.entity.FilterEntity;
import com.wisercat.demo.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok(filters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilterEntity> getFilter(@PathVariable Long id) {
        var filter = filterService.getFilter(id);
        return ResponseEntity.ok(filter);
    }

    @PostMapping
    public ResponseEntity<FilterEntity> createFilter(@RequestBody FilterEntity filter) {
        System.out.println("I AM GETTING HERE!");
        var createdFilter = filterService.createFilter(filter);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilterEntity> updateFilter(@PathVariable Long id,
                                                     @RequestBody FilterEntity filter) {
        var updatedFilter = filterService.updateFilter(id, filter);
        if (updatedFilter != null) {
            return ResponseEntity.ok(updatedFilter);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
