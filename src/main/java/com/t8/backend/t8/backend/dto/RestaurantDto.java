package com.t8.backend.t8.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String restaurantName;
    private String location;
    private String imageUrl;
    private String category;
}