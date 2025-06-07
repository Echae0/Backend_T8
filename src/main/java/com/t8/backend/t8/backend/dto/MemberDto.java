package com.t8.backend.t8.backend.dto;

import com.t8.backend.t8.backend.entity.Member; // Member.Status를 사용하기 위해 임포트

import com.t8.backend.t8.backend.security.entity.UserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Pattern(
            regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678"
    )
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
