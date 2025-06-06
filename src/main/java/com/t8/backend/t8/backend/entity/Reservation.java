package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reservationNumber; // 자동생성 R-YYYYMMDD-memberId+restaurantId+reservationId

    private Integer partySize;

    private LocalDateTime reservedAt;

    // ✅ 단일 요청사항 텍스트 필드
    @Column(length = 500)
    private String requestDetail;

    private  Integer turnTime; // restaurant 참조
    private Integer predictedWait; // restaurant 참조

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    private Duration waitingTime;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.REQUESTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = true)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Review> reviews;


    @PrePersist
    protected void onCreate() {
        if (this.reservedAt == null) {
            this.reservedAt = LocalDateTime.now();
        }
    }

    public enum Status {
        REQUESTED("요청됨"),
        JOINED("입장됨"),
        REVIEWED("리뷰작성완료"),
        CANCELLED("취소됨");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public void generateReservationNumber() {
        if (this.member == null || this.restaurant == null || this.id == null) {
            throw new IllegalStateException("member, restaurant, id must not be null when generating reservationNumber");
        }

        String datePart = this.getCreatedAt().toLocalDate().toString().replace("-", "");
        String idPart = String.format("%02d%02d%04d", member.getId(), restaurant.getId(), id);
        this.reservationNumber = "R-" + datePart + "-" + idPart;
    }
    //R-20250602-010100
    //= R-YYYYMMDD-memberId+restaurantId+reservationId
}
