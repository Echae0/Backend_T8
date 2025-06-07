package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

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

    private String imagePath;

    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reservedAt; // reservation 참조

    private LocalDateTime joinedAt; // reservation 참조

    private Duration waitingTime; // reservation 참조

    private Integer rating;

    public LocalDateTime getReservedAt() {
        return this.reservation != null ? this.reservation.getReservedAt() : null;
    }

    public LocalDateTime getJoinedAt() {
        return this.reservation != null ? this.reservation.getJoinedAt() : null;
    }

    public Duration getWaitingTime() {
        return this.reservation != null ? this.reservation.getWaitingTime() : null;
    }


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
