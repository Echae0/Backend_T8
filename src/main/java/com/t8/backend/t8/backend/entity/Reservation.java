package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Reservation extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reservationNumber;

    private Integer partySize;

    private LocalDateTime reservedAt;
    private LocalDateTime joinedAt;
    private Integer predictedWait;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.REQUESTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RequestDetail> requestDetails = new ArrayList<>();

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

    // 연관 관계 편의 메서드
    public void addRequestDetail(RequestDetail requestDetail) {
        requestDetails.add(requestDetail);
        requestDetail.setReservation(this);
    }
}
