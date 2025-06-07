package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.ReviewDto;
import com.t8.backend.t8.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private final ReviewService reviewService;

    // ✅ 예약 기준 리뷰 등록
    @PostMapping("/api/reservations/{reservationId}/reviews")
    public ResponseEntity<ReviewDto> createReview(@ModelAttribute ReviewDto dto) throws IOException {
        ReviewDto savedReview = reviewService.create(dto);
        return ResponseEntity.ok(savedReview);
    }

    // 특정 식당의 모든 리뷰 조회
    @GetMapping("/api/restaurants/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviews(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reviewService.getByRestaurantId(restaurantId));
    }

    // 특정 회원의 모든 리뷰 조회
    @GetMapping("/api/members/{memberId}/reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviewsByRestaurant(@PathVariable Long memberId) {
        return ResponseEntity.ok(reviewService.getByMemberId(memberId));
    }

    // 모든 리뷰 조회
    @GetMapping("/api/reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviewsByMember() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    // ✅ 리뷰 단건 조회
    @GetMapping("/api/reviews/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    // ✅ 리뷰 삭제
    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ 리뷰 수정
    @PutMapping("/api/reviews/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto dto) {
        ReviewDto updated = reviewService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

}