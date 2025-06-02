package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    private  Integer turnTime;
    private Integer predictedWait;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime waitingTime;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.REQUESTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = true)
    private Restaurant restaurant;



    public enum Status {
        REQUESTED("요청됨"),
        APPROVED("승인됨"),
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
