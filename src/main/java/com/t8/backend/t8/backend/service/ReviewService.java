package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.ReviewDto;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.entity.Review;
import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import com.t8.backend.t8.backend.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    // 등록
    public ReviewDto create(ReviewDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));

        Review review = Review.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .member(member)
                .restaurant(restaurant)
                .build();

        Review saved = reviewRepository.save(review);
        return toDto(saved);
    }

    // 단일 조회
    public ReviewDto getById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));
        return toDto(review);
    }

    // 전체 조회
    public List<ReviewDto> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 삭제
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    // 엔티티 → DTO
    private ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .memberId(review.getMember().getId())
                .memberName(review.getMember().getName()) // 선택
                .restaurantId(review.getRestaurant().getId())
                .restaurantName(review.getRestaurant().getRestaurantName())

                .build();
    }

    public ReviewDto update(Long id, ReviewDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        // 업데이트 가능한 필드만 수정
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        // (선택) memberId 또는 restaurantId를 바꾸고 싶다면 추가 로직이 필요함
        // 일반적으로 리뷰의 소유자나 식당은 수정하지 않는 것이 일반적이므로 생략

        return toDto(reviewRepository.save(review));
    }

}