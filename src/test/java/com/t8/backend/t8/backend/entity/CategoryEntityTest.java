package com.t8.backend.t8.backend.entity;

import com.t8.backend.t8.backend.dto.CategoryDto;
import com.t8.backend.t8.backend.service.CategoryService;
import com.t8.backend.t8.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
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
@Rollback(true) // ✅ rollback 안 하도록 설정 → 실제 DB에 저장됨
public class CategoryEntityTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void createAndViewCategories() {
        // 1. 카테고리 생성
        CategoryDto dto = CategoryDto.builder()
                .categoryCode("CAFE-" + System.currentTimeMillis()) // 유니크 값 보장
                .categoryName("카페")
                .build();

        CategoryDto saved = categoryService.register(dto);
        assertNotNull(saved.getId());
        assertEquals("카페", saved.getCategoryName());
        System.out.println("✅ 저장된 카테고리 ID: " + saved.getId());

        // 2. 전체 목록 조회 및 출력
        List<CategoryDto> all = categoryService.getAll();
        assertFalse(all.isEmpty());

        for (CategoryDto c : all) {
            System.out.println("📂 " + c.getId() + " | " + c.getCategoryCode() + " | " + c.getCategoryName());
        }

        // 3. 방금 등록한 값 포함 여부 검증
        boolean exists = all.stream().anyMatch(c -> c.getCategoryCode().equals(saved.getCategoryCode()));
        assertTrue(exists, "생성된 카테고리가 목록에 존재해야 합니다.");
    }

}