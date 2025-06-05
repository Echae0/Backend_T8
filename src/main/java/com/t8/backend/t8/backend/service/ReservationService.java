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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

        // turnTime, predictedWait 직접 계산
//        if (reservation.getRestaurant() != null) {
//            int count = reservationRepository.countByRestaurantAndStatus(
//                    reservation.getRestaurant(), Reservation.Status.REQUESTED);
//            int turnTime = count + 1;
//            reservation.setTurnTime(turnTime);
//
//            // ✅ predictedWait = turnTime × (3~5 중 랜덤값)
//            int randomMultiplier = 3 + (int)(Math.random() * 3); // 3, 4, 5
//            reservation.setPredictedWait(turnTime * randomMultiplier);
//        }

        if (reservation.getRestaurant() != null) {
            Restaurant restaurant = reservation.getRestaurant();

            // ✅ 1. 현재 유효 예약 건 수 확인
            int activeReservations = reservationRepository
                    .countByRestaurantAndStatusIn(restaurant, List.of(Reservation.Status.REQUESTED, Reservation.Status.JOINED));

            // ✅ 2. 제한 초과 여부 확인
            if (activeReservations >= restaurant.getDailyLimitedTeams()) {
                throw new IllegalStateException("예약 제한 인원 초과로 예약할 수 없습니다.");
            }

            // ✅ 레스토랑의 currentWaitingTeams + 1 을 turnTime으로 사용
            int turnTime = restaurant.getCurrentWaitingTeams() + 1;
            reservation.setTurnTime(turnTime);

            int randomMultiplier = 3 + (int)(Math.random() * 3); // 3, 4, 5
            reservation.setPredictedWait(turnTime * randomMultiplier);
        }

        reservation = reservationRepository.save(reservation);

        if (reservation.getReservationNumber() == null || reservation.getReservationNumber().isEmpty()) {
            String date = reservation.getCreatedAt().toLocalDate().toString().replace("-", "");
            Long memberId = reservation.getMember() != null ? reservation.getMember().getId() : 0L;
            Long restaurantId = reservation.getRestaurant() != null ? reservation.getRestaurant().getId() : 0L;
            Long reservationId = reservation.getId();

            String idPart = String.format("%02d%02d%04d", memberId, restaurantId, reservationId);
            reservation.setReservationNumber("R-" + date + "-" + idPart);

            reservation = reservationRepository.save(reservation);
        }

        return toDto(reservation);
    }




//    public List<ReservationDto> getByRestaurantId(Long restaurantId) {
//        return reservationRepository.findActiveByRestaurantId(restaurantId)
//                .stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public List<ReservationDto> getByRestaurantId(Long restaurantId) {
        List<Reservation> reservations = reservationRepository.findActiveByRestaurantId(restaurantId);

        reservations.sort((r1, r2) -> r1.getCreatedAt().compareTo(r2.getCreatedAt()));

        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            int turnTime = i + 1;
            reservation.setTurnTime(turnTime);

            int randomMultiplier = 3 + (int)(Math.random() * 3); // 3, 4, 5
            reservation.setPredictedWait(turnTime * randomMultiplier);
        }

        reservationRepository.saveAll(reservations);

        return reservations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

//    public List<ReservationDto> getByMemberId(Long memberId) {
//        return reservationRepository.findByMemberId(memberId)
//                .stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public List<ReservationDto> getByMemberId(Long memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);

        Map<Restaurant, List<Reservation>> groupedByRestaurant = reservations.stream()
                .filter(r -> r.getRestaurant() != null && r.getStatus() == Reservation.Status.REQUESTED)
                .collect(Collectors.groupingBy(Reservation::getRestaurant));

        for (Map.Entry<Restaurant, List<Reservation>> entry : groupedByRestaurant.entrySet()) {
            Restaurant restaurant = entry.getKey();
            List<Reservation> activeReservations = reservationRepository.findActiveByRestaurantId(restaurant.getId());

            activeReservations.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));

            for (int i = 0; i < activeReservations.size(); i++) {
                Reservation r = activeReservations.get(i);
                int turnTime = i + 1;
                r.setTurnTime(turnTime);

                int randomMultiplier = 3 + (int)(Math.random() * 3); // 3, 4, 5
                r.setPredictedWait(turnTime * randomMultiplier);
            }

            reservationRepository.saveAll(activeReservations);
        }

        return reservations.stream().map(this::toDto).collect(Collectors.toList());
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

        // 기존 상태 저장
        Reservation.Status oldStatus = reservation.getStatus();
        Reservation.Status newStatus = Reservation.Status.valueOf(dto.getStatus());

        // 필드 업데이트
        reservation.setPartySize(dto.getPartySize());
        reservation.setRequestDetail(dto.getRequestDetail());
        reservation.setTurnTime(dto.getTurnTime());
        reservation.setPredictedWait(dto.getPredictedWait());

        // 상태 업데이트
        reservation.setStatus(newStatus);

        // ✅ 상태가 JOINED로 바뀌는 경우: joinedAt과 waitingTime 계산
        if (oldStatus != Reservation.Status.JOINED && newStatus == Reservation.Status.JOINED) {
            LocalDateTime now = LocalDateTime.now();
            reservation.setJoinedAt(now);

            if (reservation.getReservedAt() != null) {
                Duration waitingDuration = Duration.between(reservation.getReservedAt(), now);
                reservation.setWaitingTime(waitingDuration.isNegative() ? Duration.ZERO : waitingDuration);
            } else {
                reservation.setWaitingTime(Duration.ZERO);
            }
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