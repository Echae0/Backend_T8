package com.t8.backend.t8.backend.entity;

import com.t8.backend.t8.backend.repository.MenuRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 내장 DB 대신 실제 DB
@ActiveProfiles("prod")                                                   // prod 프로파일로 구동
@Transactional
@Rollback(false)                                                           // 롤백 막기
public class MenuEntityTest {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testSaveMenuToMariaDB() {
        // 1) Restaurant 저장
        Restaurant rest = Restaurant.builder()
                .restaurantName("REAL_DB_TEST")
                .location("테스트시")
                .contactNumber("010-0000-0000")
                .openingHours("09:00-18:00")
                .build();
        rest = restaurantRepository.save(rest);

        // 2) Menu 저장
        Menu menu = Menu.builder()
                .name("실제DB_메뉴")
                .description("MariaDB 지속성 확인")
                .price(BigDecimal.valueOf(5000))
                .available(true)
                .imageUrl("http://example.com/prod-menu.jpg")
                .restaurant(rest)
                .build();
        Menu saved = menuRepository.save(menu);

        // 3) 기본 필드 검증
        assertNotNull(saved.getId(), "ID가 할당되어야 한다");
        assertEquals("실제DB_메뉴", saved.getName());
        assertEquals(BigDecimal.valueOf(5000), saved.getPrice());

        // 4) 연관관계 검증
        Optional<Menu> found = menuRepository.findById(saved.getId());
        assertTrue(found.isPresent(), "저장된 메뉴를 찾을 수 있어야 한다");
        assertEquals(rest.getId(), found.get().getRestaurant().getId(),
                "연관된 식당 ID가 일치해야 한다");

        // 5) BaseEntity 감사 필드 검증
        Menu fetched = found.get();
        assertNotNull(fetched.getCreatedAt(), "createdAt이 설정되어야 한다");
        assertNotNull(fetched.getUpdatedAt(), "updatedAt이 설정되어야 한다");
    }
}