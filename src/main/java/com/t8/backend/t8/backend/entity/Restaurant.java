package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(
        name = "restaurants",
        indexes = {
                @Index(name = "idx_restaurant_name", columnList = "restaurantName"),
                @Index(name = "idx_location", columnList = "location")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String restaurantName;

    private String location;
    private String imageUrl;
    private String contactNumber;
    private String openingHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /** 평균 평점 */
    @Builder.Default
    private Double averageRating = 0.0;

    /** 일일 최대 예약 팀 수 */
    @Builder.Default
    private Integer dailyLimitedTeams = 10;

    /** 남은 예약 가능 팀 수 */
    @Builder.Default
    private Integer availableTeams = 10;

    /** 예약들 */
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    /** 리뷰들 */
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    /** 메뉴들 */
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Menu> menus = new ArrayList<>();

    // 편의 메서드: 양방향 연관관계 설정
    public void addReservation(Reservation r) {
        reservations.add(r);
        r.setRestaurant(this);
    }
    public void addReview(Review r) {
        reviews.add(r);
        r.setRestaurant(this);
        // 평균 평점 재계산
        this.averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
    public void addMenu(Menu m) {
        menus.add(m);
        m.setRestaurant(this);
    }
}

