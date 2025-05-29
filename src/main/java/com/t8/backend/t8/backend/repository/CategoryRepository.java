package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryCode(String code);
}
