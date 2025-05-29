package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.ReservationDto;
import com.t8.backend.t8.backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService service;

    @Autowired
    public ReservationController(ReservationService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody ReservationDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationDto> updateStatus(@PathVariable Long id,
                                                       @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        service.cancel(id);
        return ResponseEntity.noContent().build();
    }
}