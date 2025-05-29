package com.t8.backend.t8.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;                  // 응답 시 사용
    private Integer rating;          // 공통 (입력 + 출력)
    private String comment;          // 공통 (입력 + 출력)
    private LocalDateTime createdAt; // 응답 시 사용

    private Long memberId;           // 입력 시 사용
    private String memberName;       // 응답 시 사용

    private Long restaurantId;       // 입력 시 사용
    private String restaurantName;   // 응답 시 사용
}
