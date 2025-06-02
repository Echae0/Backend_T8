package com.t8.backend.t8.backend.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;                  // 응답 시 사용
    private String imageUrl;
    private String comment;          // 공통 (입력 + 출력)
    private LocalDateTime reservedAt;
    private LocalDateTime joinedAt;
    private Duration waitingTime;
    private Integer rating;          // 공통 (입력 + 출력)


    private Long memberId;           // 입력 시 사용
    private String memberName;       // 응답 시 사용

    private Long restaurantId;       // 입력 시 사용
    private String restaurantName;   // 응답 시 사용

    private Long reservationId;       // 추가: 입력 시 사용
    private String reservationNumber; // 추가: 응답 시 사용


}