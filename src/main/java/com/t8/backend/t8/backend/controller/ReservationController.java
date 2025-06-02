package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.ReservationDto;
import com.t8.backend.t8.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // ✅ 특정 식당에 예약 생성
    @PostMapping("/api/restaurants/{restaurantId}/reservations")
    public ResponseEntity<ReservationDto> create(
            @PathVariable Long restaurantId,
            @RequestBody ReservationDto dto
    ) {
        dto.setRestaurantId(restaurantId);  // restaurantId를 DTO에 세팅
        ReservationDto created = reservationService.create(dto);
        return ResponseEntity.ok(created);
    }

    // ✅ 모든 예약 조회
    @GetMapping("/api/reservations")
    public ResponseEntity<List<ReservationDto>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/api/restaurants/{restaurantId}/reservations")
    public ResponseEntity<List<ReservationDto>> getByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reservationService.getByRestaurantId(restaurantId));
    }

    // ✅ 특정 멤버의 예약 목록 조회
    @GetMapping("/api/members/{memberId}/reservations")
    public ResponseEntity<List<ReservationDto>> getByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(reservationService.getByMemberId(memberId));
    }


    // ✅ 단건 예약 조회
    @GetMapping("/api/reservations/{id}")
    public ResponseEntity<ReservationDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    // ✅ 예약 수정
    @PutMapping("/api/reservations/{id}")
    public ResponseEntity<ReservationDto> update(
            @PathVariable Long id,
            @RequestBody ReservationDto dto
    ) {
        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    // ✅ 예약 취소
    @PostMapping("/api/reservations/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        reservationService.cancel(id);
        return ResponseEntity.ok().build();
    }

    // ✅ 예약 삭제
    @DeleteMapping("/api/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
