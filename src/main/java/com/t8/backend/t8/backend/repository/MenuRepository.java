package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    /** 특정 식당의 메뉴 목록 조회 */
    List<Menu> findByRestaurantId(Long restaurantId);
}