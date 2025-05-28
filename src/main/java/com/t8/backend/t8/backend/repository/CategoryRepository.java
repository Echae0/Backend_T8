// CategoryRepository.java
package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
