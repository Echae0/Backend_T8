package com.t8.backend.t8.backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String categoryCode;
    private String categoryName;
    private Long parentId; // 상위 카테고리와의 관계
    private List<CategoryDto> subCategories;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
