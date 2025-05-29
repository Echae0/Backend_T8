package com.t8.backend.t8.backend.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MenuDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String name;

        @Size(max = 1000)
        private String description;

        @NotNull @DecimalMin("0.0")
        private BigDecimal price;
        private String imageUrl;

        @NotNull
        private Boolean available;


    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Boolean available;
        private Long restaurantId;
        private String restaurantName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
