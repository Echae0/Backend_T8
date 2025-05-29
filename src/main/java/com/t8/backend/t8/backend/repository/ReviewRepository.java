// ReviewRepository.java
package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
