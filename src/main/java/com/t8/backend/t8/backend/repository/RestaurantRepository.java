// RestaurantRepository.java
package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
