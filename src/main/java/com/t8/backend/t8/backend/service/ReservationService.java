package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.ReservationDto;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Reservation;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.repository.ReservationRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberRepository memberRepository,
                              RestaurantRepository restaurantRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.restaurantRepository = restaurantRepository;
    }

    private Reservation toEntity(ReservationDto dto) {
        Member member = dto.getMemberId() != null ? memberRepository.findById(dto.getMemberId()).orElse(null) : null;
        Restaurant restaurant = dto.getRestaurantId() != null ? restaurantRepository.findById(dto.getRestaurantId()).orElse(null) : null;

        return Reservation.builder()
                .reservationNumber(dto.getReservationNumber())
                .partySize(dto.getPartySize())
                .reservedAt(dto.getReservedAt())
                .joinedAt(dto.getJoinedAt())
                .predictedWait(dto.getPredictedWait())
                .status(Reservation.Status.REQUESTED)
                .member(member)
                .restaurant(restaurant)
                .build();
    }

    private ReservationDto toDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .reservationNumber(reservation.getReservationNumber())
                .partySize(reservation.getPartySize())
                .reservedAt(reservation.getReservedAt())
                .joinedAt(reservation.getJoinedAt())
                .predictedWait(reservation.getPredictedWait())
                .status(reservation.getStatus().name())
                .memberId(reservation.getMember() != null ? reservation.getMember().getId() : null)
                .restaurantId(reservation.getRestaurant() != null ? reservation.getRestaurant().getId() : null)
                .build();
    }

    @Transactional
    public ReservationDto create(ReservationDto dto) {
        Reservation reservation = toEntity(dto);
        if (reservation.getReservationNumber() == null || reservation.getReservationNumber().isEmpty()) {
            reservation.setReservationNumber("R-" + System.currentTimeMillis());
        }
        return toDto(reservationRepository.save(reservation));
    }

    public ReservationDto getById(Long id) {
        return reservationRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + id));
    }

    public List<ReservationDto> getAll() {
        return reservationRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ReservationDto update(Long id, ReservationDto dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + id));

        reservation.setPartySize(dto.getPartySize());
        reservation.setReservedAt(dto.getReservedAt());
        reservation.setJoinedAt(dto.getJoinedAt());
        reservation.setPredictedWait(dto.getPredictedWait());

        return toDto(reservationRepository.save(reservation));
    }

    @Transactional
    public void cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + id));
        reservation.setStatus(Reservation.Status.CANCELLED);
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}