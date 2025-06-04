package com.t8.backend.t8.backend.security.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpRequestDto {
//    private Long id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate; // "YYYY-MM-DD"

}