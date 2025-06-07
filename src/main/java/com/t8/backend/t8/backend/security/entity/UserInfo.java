package com.t8.backend.t8.backend.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

//    private String phoneNumber;
//
//    private String address;
//
//    private LocalDate birthDate;

    private String roles;
}