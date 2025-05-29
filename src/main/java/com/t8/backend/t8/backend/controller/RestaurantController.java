package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.RestaurantDto;
import com.t8.backend.t8.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService service;

    @Autowired
    public RestaurantController(RestaurantService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<RestaurantDto> create(@RequestBody RestaurantDto dto) {
        return ResponseEntity.ok(service.register(dto));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> update(@PathVariable Long id, @RequestBody RestaurantDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}