package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.MenuDto;
import com.t8.backend.t8.backend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /** 1) 전역 전체 메뉴 조회 */
    @GetMapping("/menus")
    public ResponseEntity<List<MenuDto.Response>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }

    /** 2) 특정 식당의 메뉴 목록 조회 */
    @GetMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<List<MenuDto.Response>> listByRestaurant(
            @PathVariable Long restaurantId
    ) {
        return ResponseEntity.ok(menuService.getMenusByRestaurant(restaurantId));
    }

    /** 3) 메뉴 등록 */
    @PostMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<MenuDto.Response> createMenu(
            @PathVariable Long restaurantId,
            @RequestBody @Valid MenuDto.Request request
    ) {
        MenuDto.Response created = menuService.createMenu(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** 4) 단일 메뉴 조회 */
    @GetMapping("/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<MenuDto.Response> getMenu(
            @PathVariable Long restaurantId,
            @PathVariable Long menuId
    ) {
        return ResponseEntity.ok(menuService.getMenu(menuId));
    }

    /** 5) 메뉴 수정 */
    @PutMapping("/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<MenuDto.Response> updateMenu(
            @PathVariable Long restaurantId,
            @PathVariable Long menuId,
            @RequestBody @Valid MenuDto.Request request
    ) {
        MenuDto.Response updated = menuService.updateMenu(menuId, request);
        return ResponseEntity.ok(updated);
    }

    /** 6) 메뉴 삭제 */
    @DeleteMapping("/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long restaurantId,
            @PathVariable Long menuId
    ) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}

