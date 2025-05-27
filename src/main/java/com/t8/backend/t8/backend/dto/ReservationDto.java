package com.t8.backend.t8.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long id;
    private String reservationNumber;
    private Integer partySize;
    private LocalDateTime reservedAt;
    private String status;
    private Long memberId;
    private Long restaurantId;
}