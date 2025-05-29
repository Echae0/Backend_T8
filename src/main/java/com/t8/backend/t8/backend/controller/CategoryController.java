package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.CategoryDto;
import com.t8.backend.t8.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        return ResponseEntity.ok(service.register(dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> list() {
        return ResponseEntity.ok(service.getAll());
    }
}