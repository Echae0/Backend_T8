package com.t8.backend.t8.backend.dto;

import com.t8.backend.t8.backend.entity.Member; // Member.Status를 사용하기 위해 임포트

import com.t8.backend.t8.backend.security.entity.UserInfo;
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
    @Builder.Default
    private Member.Status status = Member.Status.ACTIVE;
    @Builder.Default
    private Integer maxReservationCount = 0;
    @Builder.Default
    private Integer noshowCounts = 0;
    private LocalDateTime createdAt; // BaseEntity 필드 추가
    private LocalDateTime updatedAt; // BaseEntity 필드 추가
//    private UserInfo userInfo;
}
