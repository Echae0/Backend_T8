package com.t8.backend.t8.backend.entity;

import com.t8.backend.t8.backend.repository.CategoryRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("prod") // 실제 MariaDB 연결되는 설정으로 작동
@Transactional
@Rollback(false) // DB에 실제로 저장되도록
public class RestaurantEntityTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveRestaurant() {
        // 테스트용 카테고리 생성 및 저장 (외래키 필요)
        Category category = Category.builder()
                .categoryName("한식")
                .build();
        category = categoryRepository.save(category);

        // Restaurant 엔티티 생성
        Restaurant restaurant = Restaurant.builder()
                .restaurantName("맛있는 식당")
                .location("서울 강남구")
                .imageUrl("https://example.com/image.jpg")
                .contactNumber("02-123-4567")
                .openingHours("10:00~22:00")
                .category(category)
                .dailyLimitedTeams(15)
                .availableTeams(15)
                .build();

        // 저장
        Restaurant saved = restaurantRepository.save(restaurant);

        // 검증
        assertNotNull(saved.getId(), "식당 ID가 null이면 안 됩니다.");
        assertEquals("맛있는 식당", saved.getRestaurantName());
        assertEquals("서울 강남구", saved.getLocation());
        assertEquals("한식", saved.getCategory().getCategoryName());
    }
}
