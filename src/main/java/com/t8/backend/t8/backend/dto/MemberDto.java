package com.t8.backend.t8.backend.dto;

import com.t8.backend.t8.backend.entity.Member; // Member.Status를 사용하기 위해 임포트

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime; // createdAt, updatedAt을 위해 추가

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;
//    private String memberNumber;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
    private Member.Status status; // Member 엔티티의 Status Enum 사용
    private Integer maxReservationCount;
    private Integer noshowCounts;
    private LocalDateTime createdAt; // BaseEntity 필드 추가
    private LocalDateTime updatedAt; // BaseEntity 필드 추가
}
