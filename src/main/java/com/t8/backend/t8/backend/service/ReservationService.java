package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.ReservationRequestDTO;
import com.t8.backend.t8.backend.dto.ReservationResponseDTO;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Reservation;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.repository.ReservationRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public ReservationResponseDTO createReservation(ReservationRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Reservation reservation = Reservation.builder()
                .reservationNumber(requestDTO.getReservationNumber())
                .partySize(requestDTO.getPartySize())
                .reservedAt(requestDTO.getReservedAt())
                .joinedAt(requestDTO.getJoinedAt())
                .predictedWait(requestDTO.getPredictedWait())
                .status(Reservation.Status.valueOf(requestDTO.getStatus()))
                .member(member)
                .restaurant(restaurant)
                .build();

        reservationRepository.save(reservation);

        return toResponseDTO(reservation);
    }

    public ReservationResponseDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        return toResponseDTO(reservation);
    }

    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReservationResponseDTO updateReservation(Long id, ReservationRequestDTO requestDTO) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        reservation.setReservationNumber(requestDTO.getReservationNumber());
        reservation.setPartySize(requestDTO.getPartySize());
        reservation.setReservedAt(requestDTO.getReservedAt());
        reservation.setJoinedAt(requestDTO.getJoinedAt());
        reservation.setPredictedWait(requestDTO.getPredictedWait());
        reservation.setStatus(Reservation.Status.valueOf(requestDTO.getStatus()));
        reservation.setMember(member);
        reservation.setRestaurant(restaurant);

        reservationRepository.save(reservation);

        return toResponseDTO(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservationRepository.delete(reservation);
    }

    private ReservationResponseDTO toResponseDTO(Reservation reservation) {
        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .reservationNumber(reservation.getReservationNumber())
                .partySize(reservation.getPartySize())
                .reservedAt(reservation.getReservedAt())
                .joinedAt(reservation.getJoinedAt())
                .predictedWait(reservation.getPredictedWait())
                .status(reservation.getStatus().name())
                .memberId(reservation.getMember().getId())
                .restaurantId(reservation.getRestaurant().getId())
                .build();
    }
}
