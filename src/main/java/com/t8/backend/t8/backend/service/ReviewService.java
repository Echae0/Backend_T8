package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.ReviewDto;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Reservation;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.entity.Review;
import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.repository.ReservationRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import com.t8.backend.t8.backend.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;

    @Value("${upload.path}")
    private String uploadDir;


    // 등록
    public ReviewDto create(ReviewDto dto) throws IOException {
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new EntityNotFoundException("예약을 찾을 수 없습니다."));

        Member member = reservation.getMember();
        Restaurant restaurant = reservation.getRestaurant();

        String savedImageFileName = null;

        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            savedImageFileName = saveImageFile(dto.getImageFile(), uploadDir);
        }
//        String savedImageFileName = saveImageFile(dto.getImageFile(), uploadDir);



        Review review = Review.builder()
                .imagePath(savedImageFileName)
                .comment(dto.getComment())
                .reservedAt(dto.getReservedAt())
                .joinedAt(dto.getJoinedAt())
                .waitingTime(dto.getWaitingTime())
                .rating(dto.getRating())
                .member(member)
                .restaurant(restaurant)
                .reservation(reservation)
                .build();

        restaurant.addReview(review);
        Review saved = reviewRepository.save(review);

        return toDto(saved);
    }

    // 단일 조회
    public ReviewDto getById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));
        return toDto(review);
    }

    public List<ReviewDto> getByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));

        return restaurant.getReviews()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다."));

        return member.getReviews()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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

    private Review toEntity(ReviewDto dto) {
        return Review.builder()
                .comment(dto.getComment())
                .reservedAt(dto.getReservedAt())
                .joinedAt(dto.getJoinedAt())
                .waitingTime(dto.getWaitingTime())
                .rating(dto.getRating())
                .build();
    }

    // 엔티티 → DTO
    private ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .imagePath(review.getImagePath())
                .rating(review.getRating())
                .comment(review.getComment())
                .reservedAt(review.getReservedAt())
                .joinedAt(review.getJoinedAt())
                .waitingTime(review.getWaitingTime())
                .memberId(review.getMember().getId())
                .memberName(review.getMember().getName()) // 선택
                .restaurantId(review.getRestaurant().getId())
                .restaurantName(review.getRestaurant().getRestaurantName())
                .reservationId(review.getReservation().getId())
                .reservationNumber(review.getReservation().getReservationNumber())
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

    // 파일 저장 유틸리티 메서드 (서비스 내부에 두는게 일반적)
    private String saveImageFile(MultipartFile file, String uploadDir) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String ext = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String savedFileName = java.util.UUID.randomUUID().toString() + ext;
        Path path = Paths.get(uploadDir, savedFileName);

        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        file.transferTo(path.toFile());

        return savedFileName;
    }

    public Page<ReviewDto> getReviewsByMemberWithPaging(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.findByMemberId(memberId, pageable);

        return reviewPage.map(this::toDto); // toDto는 Review → ReviewDto 변환 메서드
    }

}