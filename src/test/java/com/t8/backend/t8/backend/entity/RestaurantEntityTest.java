package com.t8.backend.t8.backend.entity;

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

    @Test
    void testSaveRestaurantWithoutCategory() {
        // Category 없이 Restaurant 생성
        Restaurant restaurant = Restaurant.builder()
                .restaurantName("Dummy Restaurant")
                .location("Busan")
                .imageUrl("http://dummyimage.com/dummy.jpg")
                .category(null) // Category 없이 저장
                .contactNumber("051-111-2222")
                .openingHours("10:00-22:00")
                .averageRating(4.5)
                .dailyLimitedTeams(10)
                .availableTeams(8)
                .build();

        // 저장
        Restaurant saved = restaurantRepository.save(restaurant);
        assertNotNull(saved.getId(), "Restaurant 저장 실패");

        // 조회 및 필드 검증
        Restaurant found = restaurantRepository.findById(saved.getId())
                .orElseThrow(() -> new AssertionError("레스토랑 조회 실패"));

        assertEquals("Dummy Restaurant", found.getRestaurantName());
        assertEquals("Busan", found.getLocation());
        assertEquals("http://dummyimage.com/dummy.jpg", found.getImageUrl());
        assertEquals("051-111-2222", found.getContactNumber());
        assertEquals("10:00-22:00", found.getOpeningHours());
        assertEquals(4.5, found.getAverageRating());
        assertEquals(10, found.getDailyLimitedTeams());
        assertEquals(8, found.getAvailableTeams());

        // Auditing 확인
        assertNotNull(found.getCreatedAt(), "createdAt is null");
        assertNotNull(found.getUpdatedAt(), "updatedAt is null");
    }
}
