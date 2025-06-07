package com.t8.backend.t8.backend.entity;

import com.t8.backend.t8.backend.security.entity.UserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "members")
@EqualsAndHashCode(callSuper = true)
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, unique = true)
//    private String memberNumber;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(
            regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678"
    )
    private String phoneNumber;

    private String address;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Builder.Default
    private Integer maxReservationCount = 0;

    @Builder.Default
    private Integer noshowCounts = 0;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

//    // 연관 관계 편의 메서드
//    public void addReservation(Reservation reservation) {
//        reservations.add(reservation);
//        reservation.setMember(this);
//    }
//
//    public void removeReservation(Reservation reservation) {
//        reservations.remove(reservation);
//        reservation.setMember(null);
//    }

    public enum Status {
        ACTIVE("활성"),
        SUSPENDED("정지"),
        WITHDRAWN("탈퇴");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    public boolean hasRequestStatusReservation() {
        return this.reservations.stream()
                .anyMatch(reservation -> reservation.getStatus() == Reservation.Status.REQUESTED);
    }

}