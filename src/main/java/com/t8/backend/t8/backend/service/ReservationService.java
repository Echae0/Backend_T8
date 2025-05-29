package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.ReservationDto;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Reservation;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private Reservation toEntity(ReservationDto dto) {
        return Reservation.builder()
                .id(dto.getId())
                .reservationNumber(dto.getReservationNumber())
                .partySize(dto.getPartySize())
                .reservedAt(dto.getReservedAt())
                .status(Reservation.Status.valueOf(dto.getStatus())) // 🔁 String → Enum
                .member(Member.builder().id(dto.getMemberId()).build())
                .restaurant(Restaurant.builder().id(dto.getRestaurantId()).build())
                .build();
    }

    private ReservationDto toDto(Reservation entity) {
        return ReservationDto.builder()
                .id(entity.getId())
                .reservationNumber(entity.getReservationNumber())
                .partySize(entity.getPartySize())
                .reservedAt(entity.getReservedAt())
                .status(entity.getStatus().name()) // 🔁 Enum → String
                .memberId(entity.getMember().getId())
                .restaurantId(entity.getRestaurant().getId())
                .build();
    }

    @Transactional
    public ReservationDto create(ReservationDto dto) {
        Reservation reservation = toEntity(dto);
        return toDto(reservationRepository.save(reservation));
    }

    public ReservationDto getById(Long id) {
        return reservationRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + id));
    }

    public List<ReservationDto> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationDto updateStatus(Long id, String status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + id));
        try {
            reservation.setStatus(Reservation.Status.valueOf(status)); // 🔁 String → Enum
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid reservation status: " + status);
        }
        return toDto(reservationRepository.save(reservation));
    }

    @Transactional
    public void cancel(Long id) {
        reservationRepository.deleteById(id);
    }
}
