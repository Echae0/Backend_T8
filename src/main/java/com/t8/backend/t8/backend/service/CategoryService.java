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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private CategoryDto toDto(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .categoryCode(entity.getCategoryCode())
                .categoryName(entity.getCategoryName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private Category toEntity(CategoryDto dto) {
        return Category.builder()
                .id(dto.getId())
                .categoryCode(dto.getCategoryCode())
                .categoryName(dto.getCategoryName())
                .build();
    }

    @Transactional
    public CategoryDto register(CategoryDto dto) {
        // 중복 코드 방지
        if (categoryRepository.findByCategoryCode(dto.getCategoryCode()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 코드입니다.");
        }
        Category saved = categoryRepository.save(toEntity(dto));
        return toDto(saved);
    }

    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 카테고리가 존재하지 않습니다.");
        }
        categoryRepository.deleteById(id);
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 카테고리가 존재하지 않습니다."));

        category.setCategoryName(dto.getCategoryName());
        category.setCategoryCode(dto.getCategoryCode());

        return toDto(category); // 변경된 엔티티를 DTO로 변환하여 반환
    }
}