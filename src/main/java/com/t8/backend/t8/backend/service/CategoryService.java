package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    CategoryDto register(CategoryDto dto);
    List<CategoryDto> getAll();
}