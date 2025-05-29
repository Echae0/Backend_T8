package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.RestaurantDto;
import com.t8.backend.t8.backend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDto> register(@RequestBody RestaurantDto dto) {
        return ResponseEntity.ok(restaurantService.register(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAll() {
        return ResponseEntity.ok(restaurantService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> update(@PathVariable Long id, @RequestBody RestaurantDto dto) {
        return ResponseEntity.ok(restaurantService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
