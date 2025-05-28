package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String restaurantName;

    private String location;
    private String imageUrl;
    private String category;
    private Integer dailyLimitedTeams;
    private Integer availableTeams;

    @OneToMany(mappedBy = "restaurant")
    private List<Reservation> reservations;
}