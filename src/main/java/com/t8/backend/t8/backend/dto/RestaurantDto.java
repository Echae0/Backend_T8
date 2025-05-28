package com.t8.backend.t8.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String restaurantName;
    private String location;
    private String imageUrl;
    private String category; // 또는 categoryId로 명시적 연결도 가능

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
