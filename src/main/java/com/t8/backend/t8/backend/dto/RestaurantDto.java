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
    private String description;
    private String parking;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoryCode;
    private String contactNumber;
    private String openingHours;
    private Double averageRating;
    private Integer dailyLimitedTeams;
    private Integer availableTeams;
    private Integer currentWaitingTeams;
    private Integer PredictedWaitingTime;

}
