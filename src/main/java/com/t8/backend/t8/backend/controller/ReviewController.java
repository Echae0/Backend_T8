package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.ReviewDto;
import com.t8.backend.t8.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ✅ 리뷰 등록
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto dto) {
        ReviewDto created = reviewService.create(dto);
        return ResponseEntity.ok(created);
    }

    // ✅ 리뷰 전체 조회
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    // ✅ 리뷰 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    // ✅ 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
    // ✅ 리뷰 수정
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto dto) {
        ReviewDto updated = reviewService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

}