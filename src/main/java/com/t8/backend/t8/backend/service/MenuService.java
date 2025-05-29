package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.MenuDto;
import com.t8.backend.t8.backend.entity.Menu;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.MenuRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    /** 1) 전역 전체 메뉴 조회 */
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    /** 2) DTO 변환 포함 전체 메뉴 조회 */
    public List<MenuDto.Response> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /** 3) 메뉴 등록 */
    @Transactional
    public MenuDto.Response createMenu(Long restaurantId, MenuDto.Request request) {
        // 1) 식당 조회
        Restaurant rest = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("식당을 찾을 수 없습니다. id=" + restaurantId));

        // 2) Entity 생성 및 필드 바인딩
        Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setDescription(request.getDescription());
        menu.setPrice(request.getPrice());
        menu.setAvailable(request.getAvailable());
        menu.setImageUrl(request.getImageUrl());
        menu.setRestaurant(rest);

        // 3) 저장
        Menu saved = menuRepository.save(menu);

        // 4) DTO 변환 후 반환
        return toResponse(saved);
    }

    /** 4) 단일 메뉴 조회 */
    @Transactional(readOnly = true)
    public MenuDto.Response getMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다. id=" + menuId));
        return toResponse(menu);
    }

    /** 5) 식당별 메뉴 목록 조회 */
    @Transactional(readOnly = true)
    public List<MenuDto.Response> getMenusByRestaurant(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /** 6) 메뉴 수정 */
    @Transactional
    public MenuDto.Response updateMenu(Long menuId, MenuDto.Request request) {
        Menu existing = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다. id=" + menuId));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setAvailable(request.getAvailable());
        existing.setImageUrl(request.getImageUrl());

        Menu updated = menuRepository.save(existing);
        return toResponse(updated);
    }

    /** 7) 메뉴 삭제 */
    @Transactional
    public void deleteMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new IllegalArgumentException("메뉴를 찾을 수 없습니다. id=" + menuId);
        }
        menuRepository.deleteById(menuId);
    }

    // DTO 변환 헬퍼
    private MenuDto.Response toResponse(Menu menu) {
        return MenuDto.Response.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .imageUrl(menu.getImageUrl())
                .available(menu.isAvailable())
                .restaurantId(menu.getRestaurant().getId())
                .restaurantName(menu.getRestaurant().getRestaurantName())
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }
}
