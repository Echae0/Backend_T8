package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // (선택) 식당별 리뷰 조회
    List<Review> findByRestaurantId(Long restaurantId);

    // (선택) 회원별 리뷰 조회
    List<Review> findByMemberId(Long memberId);
}