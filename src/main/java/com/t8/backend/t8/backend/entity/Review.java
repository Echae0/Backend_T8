package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reservedAt;

    private LocalDateTime joinedAt;

    private Duration waitingTime;

    private Integer rating;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;


    @PrePersist
    @PreUpdate
    protected void preSave() {
        if (this.reservedAt == null) {
            this.reservedAt = LocalDateTime.now();
        }
        if (this.joinedAt != null && this.reservedAt != null) {
            Duration duration = Duration.between(reservedAt, joinedAt);
            this.waitingTime = duration.isNegative() ? Duration.ZERO : duration;
        }
    }
}