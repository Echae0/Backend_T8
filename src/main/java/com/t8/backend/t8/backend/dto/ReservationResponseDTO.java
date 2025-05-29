package com.t8.backend.t8.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {
    private Long id;
    private String reservationNumber;
    private Integer partySize;
    private LocalDateTime reservedAt;
    private LocalDateTime joinedAt;
    private Integer predictedWait;
    private String status;
    private Long memberId;
    private Long restaurantId;
}
