package com.t8.backend.t8.backend.entity;

import com.t8.backend.t8.backend.dto.ReviewDto;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import com.t8.backend.t8.backend.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("prod") // ✅ application-prod.yml 을 사용
@Transactional
@Rollback(false) // 실제 DB 반영
public class ReviewEntityTest {

    @Autowired private ReviewService reviewService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private RestaurantRepository restaurantRepository;

    @Test
    void testReviewIntegrationFlow() {
        // 1. 회원 및 식당 생성
        Member member = memberRepository.save(Member.builder()
//                .memberNumber("MEMBER-" + System.currentTimeMillis())
                .name("리뷰작성자")
                .email("reviewer" + System.currentTimeMillis() + "@test.com")
                .build());

        Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
                .restaurantName("리뷰테스트식당-" + System.currentTimeMillis())
                .location("서울시 리뷰구")
                .build());

        // 2. 리뷰 등록
        ReviewDto reviewDto = ReviewDto.builder()
                .rating(5)
                .comment("정말 훌륭한 식사였습니다!")
                .memberId(member.getId())
                .restaurantId(restaurant.getId())
                .build();

        ReviewDto saved = reviewService.create(reviewDto);
        assertNotNull(saved.getId());
        System.out.println("✅ 저장된 리뷰 ID: " + saved.getId());

        // 3. 전체 리뷰 조회 및 포함 여부 확인
        List<ReviewDto> allReviews = reviewService.getAll();
        assertFalse(allReviews.isEmpty());

        boolean exists = allReviews.stream()
                .anyMatch(r -> r.getId().equals(saved.getId()));
        assertTrue(exists, "등록된 리뷰가 목록에 있어야 합니다.");

        // 4. 리뷰 수정
        ReviewDto updated = reviewService.update(saved.getId(), ReviewDto.builder()
                .comment("리뷰 수정 완료")
                .rating(4)
                .build());
        assertEquals("리뷰 수정 완료", updated.getComment());

        // 5. 리뷰 삭제
//        reviewService.delete(saved.getId());
//        assertThrows(Exception.class, () -> reviewService.getById(saved.getId()));
//        System.out.println("🗑 리뷰 삭제 테스트 성공");
    }
}