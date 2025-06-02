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

import java.time.Duration;
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
                .requestDetail(dto.getRequestDetail())

                .turnTime(dto.getTurnTime())
                .predictedWait(dto.getPredictedWait())
                .joinedAt(dto.getJoinedAt())
                .waitingTime(dto.getWaitingTime())

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
                .requestDetail(reservation.getRequestDetail())

                .turnTime(reservation.getTurnTime())
                .predictedWait(reservation.getPredictedWait())
                .joinedAt(reservation.getJoinedAt())
                .waitingTime(reservation.getWaitingTime())

                .status(reservation.getStatus().name())
                .memberId(reservation.getMember() != null ? reservation.getMember().getId() : null)
                .restaurantId(reservation.getRestaurant() != null ? reservation.getRestaurant().getId() : null)
                .build();
    }

    @Transactional
    public ReservationDto create(ReservationDto dto) {
        Reservation reservation = toEntity(dto);

        // 먼저 저장하여 reservation.id 생성
        reservation = reservationRepository.save(reservation);

        // reservationNumber가 비어 있다면 자동 생성
        if (reservation.getReservationNumber() == null || reservation.getReservationNumber().isEmpty()) {
            String date = reservation.getCreatedAt().toLocalDate().toString().replace("-", "");
            Long memberId = reservation.getMember() != null ? reservation.getMember().getId() : 0L;
            Long restaurantId = reservation.getRestaurant() != null ? reservation.getRestaurant().getId() : 0L;
            Long reservationId = reservation.getId();

            // 숫자 자리수 고정: member(2), restaurant(2), reservation(4)
            String idPart = String.format("%02d%02d%04d", memberId, restaurantId, reservationId);
            reservation.setReservationNumber("R-" + date + "-" + idPart);

            // 다시 저장하여 reservationNumber 반영
            reservation = reservationRepository.save(reservation);
        }

        return toDto(reservation);
    }


    public List<ReservationDto> getByRestaurantId(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ReservationDto> getByMemberId(Long memberId) {
        return reservationRepository.findByMemberId(memberId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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
        reservation.setRequestDetail(dto.getRequestDetail());
        reservation.setTurnTime(dto.getTurnTime());
        reservation.setPredictedWait(dto.getPredictedWait());
        reservation.setJoinedAt(dto.getJoinedAt());

        // joinedAt 업데이트
        if (reservation.getReservedAt() != null) {
            long seconds = java.time.Duration.between(
                    reservation.getReservedAt(), dto.getJoinedAt()
            ).getSeconds();

            reservation.setWaitingTime(seconds < 0 ? Duration.ZERO : Duration.ofSeconds(seconds));
        }

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