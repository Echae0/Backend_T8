package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.CategoryDto;
import com.t8.backend.t8.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ✅ 카테고리 등록
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto dto) {
        CategoryDto created = categoryService.register(dto);
        return ResponseEntity.ok(created);
    }

    // ✅ 전체 카테고리 조회
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    // ✅ 단일 카테고리 조회
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    // ✅ 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    // ✅ 카테고리 수정
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        CategoryDto updated = categoryService.update(id, dto);
        return ResponseEntity.ok(updated);
    }


}
