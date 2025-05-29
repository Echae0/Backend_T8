package com.t8.backend.t8.backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private String reservationNumber;
    private Integer partySize;
    private LocalDateTime reservedAt;
    private LocalDateTime joinedAt;
    private Integer predictedWait;
    private Long memberId;
    private Long restaurantId;
    private Long id;
    private String status;
    private List<String> requestDetails; // 예: 요
}