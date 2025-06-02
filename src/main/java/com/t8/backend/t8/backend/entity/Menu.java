package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull @DecimalMin("0.0")
    @Column(nullable = false)
    private BigDecimal price;

    private String imageUrl;

    /** primitive boolean 으로 바꾸기 */
    @Builder.Default
    private boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // 연관관계 편의 메서드
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        if (restaurant.getMenus() == null) {
            restaurant.setMenus(new ArrayList<>());
        }
        if (!restaurant.getMenus().contains(this)) {
            restaurant.getMenus().add(this);
        }
    }

}