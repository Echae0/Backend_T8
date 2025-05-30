package com.t8.backend.t8.backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String restaurantName;
    private String location;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private CategoryDto category;
    private String categoryCode;
    private List<MenuDto> menus;
    private String contactNumber;
    private String openingHours;
}
