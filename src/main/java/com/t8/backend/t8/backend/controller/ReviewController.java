package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.ReviewDto;
import com.t8.backend.t8.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ✅ 특정 식당에 리뷰 등록
//    @PostMapping("/api/restaurants/{restaurantId}/reviews")
//    public ResponseEntity<ReviewDto> createReview(
//            @PathVariable Long restaurantId,
//            @RequestBody ReviewDto dto
//    ) {
//        dto.setRestaurantId(restaurantId);
//        ReviewDto created = reviewService.create(dto);
//        return ResponseEntity.ok(created);
//    }

    // ✅ 예약 기준 리뷰 등록
    @PostMapping("/api/reservations/{reservationId}/reviews")
    public ResponseEntity<ReviewDto> createReviewFromReservation(
            @PathVariable Long reservationId,
            @RequestBody ReviewDto dto
    ) {
        dto.setReservationId(reservationId); // reservationId만 입력받음
        ReviewDto created = reviewService.create(dto);
        return ResponseEntity.ok(created);
    }

    // 특정 식당의 모든 리뷰 조회
    @GetMapping("/api/restaurants/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviews(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reviewService.getByRestaurantId(restaurantId));
    }

    // 모든 리뷰 조회
    @GetMapping("/api/reviews")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
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