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
@ActiveProfiles("prod")
@Transactional
@Rollback(false)
public class RestaurantEntityTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveRestaurant() {
        // Given: Category 먼저 저장
        Category category = Category.builder()
                .categoryName("양식")
                .build();
        categoryRepository.save(category);

        // When: Restaurant 생성 및 저장
        Restaurant restaurant = Restaurant.builder()
                .restaurantName("이탈리안 하우스")
                .location("서울시 서초구")
                .imageUrl("https://example.com/image.jpg")
                .contactNumber("02-987-6543")
                .openingHours("11:00 ~ 21:00")
                .category(category)
                .dailyLimitedTeams(12)
                .availableTeams(12)
                .build();

        Restaurant saved = restaurantRepository.save(restaurant);

        // Then: 저장 여부 검증
        assertNotNull(saved.getId());
        assertEquals("이탈리안 하우스", saved.getRestaurantName());
        assertEquals("양식", saved.getCategory().getCategoryName());
    }
}
