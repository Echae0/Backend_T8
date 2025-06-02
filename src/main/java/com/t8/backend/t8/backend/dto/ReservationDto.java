package com.t8.backend.t8.backend.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long id;
    private String reservationNumber;
    private Integer partySize;
    private LocalDateTime reservedAt;
    private String requestDetail;

    private  Integer turnTime;
    private Integer predictedWait;
    private LocalDateTime joinedAt;
    private Duration waitingTime;

    private String status;
    private Long memberId;
    private Long restaurantId;
}