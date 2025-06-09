package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.ReservationDto;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Reservation;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.repository.ReservationRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// 🔴 사용하지 않는 import 제거 (선택 사항이지만 코드 정리 차원)
// import java.util.Map;
// import java.util.stream.Collectors; // Collectors는 사용 중이므로 유지
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger; // getByRestaurantId (DB 미업데이트 시) 제안에 사용

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map; // getByMemberId 에서 사용 중
import java.util.stream.Collectors; // stream().map(this::toDto).collect(Collectors.toList()) 에서 사용 중


@Service
@Transactional
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
        // 🔴 Member 또는 Restaurant 조회 실패 시 명확한 예외 처리 또는 Null 처리 정책 필요
        Member member = null;
        if (dto.getMemberId() != null) {
            member = memberRepository.findById(dto.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("🔴 Member not found with id: " + dto.getMemberId()));
        } else {
            // 🔴 MemberId가 필수라면 여기서 예외 처리. 필수가 아니라면 이 로직 유지.
            // throw new IllegalArgumentException("🔴 Member ID is required.");
        }

        Restaurant restaurant = null;
        if (dto.getRestaurantId() != null) {
            restaurant = restaurantRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new IllegalArgumentException("🔴 Restaurant not found with id: " + dto.getRestaurantId()));
        } else {
            // 🔴 RestaurantId가 필수라면 여기서 예외 처리. 필수가 아니라면 이 로직 유지.
            // throw new IllegalArgumentException("🔴 Restaurant ID is required.");
        }

        // 🔴 reservedAt이 dto에서 항상 제공되는지, 아니면 여기서 기본값을 설정할지 정책 필요
        //    예: .reservedAt(dto.getReservedAt() != null ? dto.getReservedAt() : LocalDateTime.now())
        return Reservation.builder()
                .reservationNumber(dto.getReservationNumber())
                .partySize(dto.getPartySize())
                .reservedAt(dto.getReservedAt()) // 🔴 이 값이 null일 경우 update 메소드에서 문제 발생 가능성 있음
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
        // 🔴 Member 또는 Restaurant가 null일 가능성을 고려한 ID 추출 (현재 코드는 이미 null 체크 중으로 양호)
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


        Reservation reservation = toEntity(dto); // 🔴 toEntity에서 restaurant가 null이면 아래 로직에서 NPE 발생
        Member member = reservation.getMember();

        // 1. 사용자가 이미 REQUEST 상태의 예약이 있는지 확인
        if (member.hasRequestStatusReservation()) {
            throw new IllegalStateException("이미 요청 상태의 예약이 존재합니다. 한 번에 하나의 예약만 요청할 수 있습니다.");
        }

        // 🔴 reservation.getRestaurant()가 null이 아님을 보장하거나, null 체크 후 로직 진행
        if (reservation.getRestaurant() != null) {
            Restaurant restaurant = reservation.getRestaurant();
            LocalDate today = LocalDate.now(); // 현재 날짜 가져오기

            // 레스토랑 일일 제한 팀 수 확인
            int dailyLimitedTeams = restaurant.getDailyLimitedTeams() != null ? restaurant.getDailyLimitedTeams() : 0;
            if (dailyLimitedTeams <= 0) {
                throw new IllegalStateException("레스토랑의 일일 예약 제한이 설정되지 않았습니다.");
            }

            int activeReservationsToday = reservationRepository
                    .countByRestaurantAndReservedAtDateAndStatusIn(
                            restaurant,
                            LocalDateTime.now(), // 현재 날짜+시간 전달
                            List.of(Reservation.Status.REQUESTED, Reservation.Status.JOINED)
                    );

            if (activeReservationsToday >= dailyLimitedTeams) {
                throw new IllegalStateException("오늘의 예약 제한 인원을 초과하여 예약할 수 없습니다.");
            }

            int activeReservations = reservationRepository
                    .countByRestaurantAndStatusIn(restaurant, List.of(Reservation.Status.REQUESTED, Reservation.Status.JOINED));

            if (activeReservations >= dailyLimitedTeams && dailyLimitedTeams > 0) { // 🔴 dailyLimitedTeams가 0일때 무조건 초과하는것 방지
                throw new IllegalStateException("예약 제한 인원 초과로 예약할 수 없습니다.");
            }

            // 🔴 NullPointerException 발생 지점! getCurrentWaitingTeams() 반환값이 null일 수 있음.
            Integer currentWaitingTeams = restaurant.getCurrentWaitingTeams();
            if (currentWaitingTeams == null) {
                // 🔴 에러 로그를 남기거나, 비즈니스 로직에 따라 기본값 처리 또는 예외 발생
                System.err.println("🔴 Warning: currentWaitingTeams is null for restaurant ID: " + restaurant.getId() + ". Defaulting to 0.");
                currentWaitingTeams = 0; // 예: null일 경우 0으로 가정
                // 또는 throw new IllegalStateException("현재 대기 팀 정보를 가져올 수 없습니다.");
            }
            int turnTime = currentWaitingTeams + 1; // 이제 currentWaitingTeams는 null이 아님
            reservation.setTurnTime(turnTime);
            // 🔴 예상 대기 시간 산정 로직 일관성 확인 (getByMemberId와 비교)
            reservation.setPredictedWait(turnTime * 4);
        } else {
            // 🔴 식당 정보가 없는 예약은 어떻게 처리할지 정책 필요
            // throw new IllegalArgumentException("Restaurant information is required to create a reservation.");
            // 또는 특정 로직 수행
            System.err.println("🔴 Warning: Reservation created without restaurant information.");
        }


        // 🔴 첫 번째 save: ID 생성 및 createdAt 등 자동 생성 필드 값 할당 목적
        // reservation = reservationRepository.save(reservation); // 아래에서 다시 save 하므로, 여기서는 생략 가능하나 명시적으로 ID를 먼저 받고 싶다면 유지

        // 🔴 예약 번호 생성 전에 ID가 할당되어 있어야 함. createdAt도 마찬가지.
        //    JPA는 save() 호출 시 또는 트랜잭션 커밋 시점에 ID를 할당.
        //    만약 reservation.getId()가 null이고 createdAt도 null이면 아래 로직에서 NPE 발생 가능.
        //    보통은 첫 save 이후 ID와 createdAt이 채워짐.
        reservation = reservationRepository.save(reservation); // ID 및 createdAt 생성을 위해 먼저 save

        if (reservation.getReservationNumber() == null || reservation.getReservationNumber().isEmpty()) {
            // 🔴 reservation.getCreatedAt()이 null일 경우 NPE 발생
            if (reservation.getCreatedAt() == null) {
                // 🔴 createdAt이 없는 경우에 대한 처리. 보통 @CreationTimestamp 등으로 자동 생성되지만, 확인 필요.
                // throw new IllegalStateException("Reservation createdAt is null. Cannot generate reservation number.");
                // 또는 기본값 사용 (그러나 이는 데이터 정합성에 문제 야기 가능)
                reservation.setCreatedAt(LocalDateTime.now()); // 임시 방편
                System.err.println("🔴 Warning: Reservation createdAt was null. Setting to current time for reservation number generation.");
            }
            String date = reservation.getCreatedAt().toLocalDate().toString().replace("-", "");

            // 🔴 member 또는 restaurant가 null일 경우 getId()에서 NPE 발생 가능 (toEntity에서 이미 예외처리했다면 괜찮음)
            Long memberId = (reservation.getMember() != null && reservation.getMember().getId() != null) ? reservation.getMember().getId() : 0L;
            Long restaurantId = (reservation.getRestaurant() != null && reservation.getRestaurant().getId() != null) ? reservation.getRestaurant().getId() : 0L;
            Long reservationId = reservation.getId(); // 이 시점에는 ID가 있어야 함

            if (reservationId == null) {
                // 🔴 ID가 여전히 null이라면 예약 번호 생성 불가
                throw new IllegalStateException("🔴 Reservation ID is null after save. Cannot generate reservation number.");
            }

            String idPart = String.format("%02d%02d%04d", memberId, restaurantId, reservationId);
            reservation.setReservationNumber("R-" + date + "-" + idPart);

            // 🔴 예약 번호 설정 후 다시 save
            reservation = reservationRepository.save(reservation);
        }

        return toDto(reservation);
    }


    @Transactional
    public List<ReservationDto> getByRestaurantId(Long restaurantId) {
        if (restaurantId == null) {
            throw new IllegalArgumentException("🔴 Restaurant ID cannot be null.");
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("🔴 Restaurant not found with id: " + restaurantId));

        // 상태가 REQUESTED인 예약들 조회
        List<Reservation> reservations = reservationRepository.findByRestaurantAndStatusIn(
                restaurant, List.of(Reservation.Status.REQUESTED));

        int currentWT = reservations.size(); // 현재 대기 팀 수

        // 각 예약에 대해 turnTime 및 predictedWait 설정
        int turnTime = 1;
        for (Reservation reservation : reservations) {
            reservation.setTurnTime(turnTime);
            reservation.setPredictedWait(turnTime * 4); // 🔴 일관성 확인 필요
            turnTime++;
        }

        reservationRepository.saveAll(reservations);

        return reservations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }




    @Transactional // 🔴 getBy... 메소드에서 DB를 수정한다면 readOnly=false여야 함
    public List<ReservationDto> getByMemberId(Long memberId) {
        // 🔴 회원 ID가 null이거나 유효하지 않은 경우에 대한 처리 추가 가능
        if (memberId == null) {
            throw new IllegalArgumentException("🔴 Member ID cannot be null.");
        }
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);

        // 🔴 NPE 방지: filter 조건에서 r.getRestaurant()가 null 체크 후 r.getStatus() 접근
        Map<Restaurant, List<Reservation>> groupedByRestaurant = reservations.stream()
                .filter(r -> r.getRestaurant() != null && r.getStatus() != null && r.getStatus() == Reservation.Status.REQUESTED)
                .collect(Collectors.groupingBy(Reservation::getRestaurant));

        for (Map.Entry<Restaurant, List<Reservation>> entry : groupedByRestaurant.entrySet()) {
            Restaurant restaurant = entry.getKey(); // 🔴 restaurant는 null이 아님 (filter 조건 덕분)
            // 🔴 restaurant.getId()가 null일 가능성은 낮지만, Restaurant 객체 생성 방식에 따라 확인 필요
            if (restaurant.getId() == null) {
                System.err.println("🔴 Warning: Restaurant in group has null ID.");
                continue; // 또는 예외 처리
            }
            List<Reservation> activeReservations = reservationRepository.findByRestaurantAndStatusIn(restaurant, List.of(Reservation.Status.REQUESTED));

//

            for (int i = 0; i < activeReservations.size(); i++) {
                Reservation r = activeReservations.get(i);
                int turnTime = i + 1;
                r.setTurnTime(turnTime);

                // 🔴 예상 대기 시간 산정 로직 일관성 확인 (create, getByRestaurantId와 비교)
                //    Math.random() 사용은 테스트 용이성을 떨어뜨릴 수 있음.
//                int randomMultiplier = 3 + (int)(Math.random() * 3); // 3, 4, 5
                r.setPredictedWait(turnTime * 4);
            }
            // 🔴 조회 메소드 내 반복적인 saveAll 호출은 성능에 큰 영향.
            //    이 로직의 필요성 및 위치 재검토 필요. (예: 특정 이벤트 발생 시에만 업데이트)
            reservationRepository.saveAll(activeReservations);
        }

        return reservations.stream().map(this::toDto).collect(Collectors.toList());
    }


    @Transactional
    public ReservationDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("🔴 Reservation ID cannot be null.");
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + id));

        // 🔴 해당 예약이 REQUESTED 상태일 경우에만 순서 계산
        if (reservation.getStatus() == Reservation.Status.REQUESTED) {
            Restaurant restaurant = reservation.getRestaurant();
            if (restaurant == null || restaurant.getId() == null) {
                throw new IllegalStateException("Reservation is not associated with a valid restaurant.");
            }

            List<Reservation> activeReservations = reservationRepository.findByRestaurantAndStatusIn(
                    restaurant, List.of(Reservation.Status.REQUESTED));

            // turnTime 및 predictedWait 재계산
            for (int i = 0; i < activeReservations.size(); i++) {
                Reservation r = activeReservations.get(i);
                int turnTime = i + 1;
                r.setTurnTime(turnTime);
                r.setPredictedWait(turnTime * 4); // 예: 4분 단위
            }

            reservationRepository.saveAll(activeReservations);
        }

        return toDto(reservation);
    }


    public List<ReservationDto> getAll() {
        return reservationRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ReservationDto update(Long id, ReservationDto dto) {
        // 🔴 ID 또는 DTO가 null인 경우에 대한 처리 추가 가능
        if (id == null) {
            throw new IllegalArgumentException("🔴 Reservation ID cannot be null.");
        }
        if (dto == null) {
            throw new IllegalArgumentException("🔴 Reservation DTO cannot be null.");
        }
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + id));

        Reservation.Status oldStatus = reservation.getStatus();
        // 🔴 dto.getStatus()가 null이거나 유효하지 않은 문자열일 경우 valueOf에서 예외 발생 가능
        Reservation.Status newStatus;
        try {
            newStatus = Reservation.Status.valueOf(dto.getStatus());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("🔴 Invalid status value: " + dto.getStatus(), e);
        }


        // 필드 업데이트 (DTO의 다른 필드들도 null 체크 또는 유효성 검사 고려)
        reservation.setPartySize(dto.getPartySize()); // 🔴 dto.getPartySize()가 Integer이고 null일 경우 NPE 발생 가능성 (primitive type으로 변경 또는 null 처리)
        reservation.setRequestDetail(dto.getRequestDetail());
        // 🔴 dto.getTurnTime() 또는 dto.getPredictedWait()가 null일 경우 NPE 발생 가능성 (Integer 타입이라면)
        if (dto.getTurnTime() != null) reservation.setTurnTime(dto.getTurnTime());
        if (dto.getPredictedWait() != null) reservation.setPredictedWait(dto.getPredictedWait());


        reservation.setStatus(newStatus);

        if (oldStatus != Reservation.Status.JOINED && newStatus == Reservation.Status.JOINED) {
            LocalDateTime now = LocalDateTime.now();
            reservation.setJoinedAt(now);

            // 🔴 reservation.getReservedAt()이 null일 경우 NPE 발생 가능성
            if (reservation.getReservedAt() != null) {
                Duration waitingDuration = Duration.between(reservation.getReservedAt(), now);
                reservation.setWaitingTime(waitingDuration.isNegative() ? Duration.ZERO : waitingDuration);
            } else {
                // 🔴 reservedAt이 null일 경우의 처리. toEntity에서 reservedAt 보장 필요.
                System.err.println("🔴 Warning: reservedAt is null for reservation ID: " + reservation.getId() + ". Setting waitingTime to ZERO.");
                reservation.setWaitingTime(Duration.ZERO);
            }
        }

        return toDto(reservationRepository.save(reservation));
    }


    @Transactional
    public void cancel(Long id) {
        // 🔴 ID가 null인 경우에 대한 처리 추가 가능
        if (id == null) {
            throw new IllegalArgumentException("🔴 Reservation ID cannot be null.");
        }
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + id));
        reservation.setStatus(Reservation.Status.CANCELLED);
        // 🔴 save 호출이 없어도 @Transactional에 의해 변경 감지(dirty checking)되어 업데이트 될 수 있으나,
        //    명시적으로 save를 호출하는 것을 고려할 수 있음 (팀/프로젝트 컨벤션에 따라).
        // reservationRepository.save(reservation);
    }

    @Transactional
    public void delete(Long id) {
        // 🔴 ID가 null인 경우에 대한 처리 추가 가능
        if (id == null) {
            throw new IllegalArgumentException("🔴 Reservation ID cannot be null.");
        }
        // 🔴 존재하지 않는 ID에 대한 deleteById는 예외를 발생시키지 않음.
        //    삭제 전 존재 여부 확인 로직 추가 가능 (비즈니스 요구사항에 따라).
        // if (!reservationRepository.existsById(id)) {
        //     throw new IllegalArgumentException("Reservation not found with id: " + id + " for deletion.");
        // }
        reservationRepository.deleteById(id);
    }
}