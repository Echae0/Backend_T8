package com.t8.backend.t8.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;
    private String address;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer maxReservationCount;
    private Integer noshowCounts;

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations;

    public enum Status { ACTIVE, SUSPENDED, WITHDRAWN }
}