package com.t8.backend.t8.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;

    private String categoryCode;

    private String categoryName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}