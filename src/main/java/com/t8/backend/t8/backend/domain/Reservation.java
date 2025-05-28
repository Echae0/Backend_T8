package com.t8.backend.t8.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reservationNumber;
    private Integer partySize;
    private LocalDateTime reservedAt;
    private LocalDateTime joinedAt;
    private Integer predictedWait;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public enum Status { REQUESTED, APPROVED, CANCELLED }
}