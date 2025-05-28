package com.t8.backend.t8.backend.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String memberNumber;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
}