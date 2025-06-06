package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants",
        indexes = {
                @Index(name = "idx_restaurant_name", columnList = "restaurantName"),
                @Index(name = "idx_location", columnList = "location")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Restaurant extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 50)
    private String restaurantName;

    @Size(max = 100)
    private String location;

    @Size(max = 300)
    private String description;

    @Size(max = 30)
    private String parking;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    private String contactNumber;

    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d-([01]\\d|2[0-3]):[0-5]\\d$")
    private String openingHours;

    @Builder.Default
    private Double averageRating = 0.0;

    @Min(1)
    @Max(100)
    private Integer dailyLimitedTeams;

    @Min(1)
    @Max(100)
    private Integer availableTeams;

    @Builder.Default
    private Integer currentWaitingTeams = 0;

    @Builder.Default
    private Integer PredictedWaitingTime = 0;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Menu> menus = new ArrayList<>();


//    // 연관 관계 편의 메서드
//    public void addReservation(Reservation reservation) {
//        reservations.add(reservation);
//        reservation.setRestaurant(this);
//    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setRestaurant(this);

        // 예약이 추가될 때마다 사용 가능 팀 수 감소
        if (availableTeams != null && availableTeams > 0) {
            availableTeams--;
        }
    }

    public void removeReservation(Reservation reservation) {
        if (reservations.remove(reservation)) {
            reservation.setRestaurant(null);
            if (availableTeams != null) {
                availableTeams++;
            }
        }
    }


    public void addReview(Review review) {
        reviews.add(review);
        review.setRestaurant(this);
        calculateAverageRating();
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        menu.setRestaurant(this);
    }

    private void calculateAverageRating() {
        this.averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
