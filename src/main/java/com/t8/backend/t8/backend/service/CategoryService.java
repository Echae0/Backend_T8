package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.CategoryDto;
import com.t8.backend.t8.backend.entity.Category;
import com.t8.backend.t8.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private Category toEntity(CategoryDto dto) {
        return Category.builder()
                .id(dto.getId())
                .categoryName(dto.getCategoryName())
                .build();
    }

    private CategoryDto toDto(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .categoryName(entity.getCategoryName())
                .build();
    }

    @Transactional
    public CategoryDto register(CategoryDto dto) {
        Category category = toEntity(dto);
        return toDto(categoryRepository.save(category));
    }

    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
}
