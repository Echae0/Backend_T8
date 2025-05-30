package com.t8.backend.t8.backend.entity;

import com.t8.backend.t8.backend.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("prod")
@Transactional
public class ReservationEntityTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @Rollback(false)
    public void testSaveAndFindReservation() {
        // 1. Reservation 엔티티 생성 (member, restaurant는 null로 처리)
        Reservation reservation = Reservation.builder()
                .reservationNumber("RES-001")
                .name("홍길동")  // 예약자 이름 추가
                .partySize(4)
                .reservedAt(LocalDateTime.now())
                .joinedAt(null)
                .predictedWait(20)
                .status(Reservation.Status.REQUESTED)
                .member(null)     // 실제 연관관계 테스트 시 Member 엔티티 필요
                .restaurant(null) // 실제 연관관계 테스트 시 Restaurant 엔티티 필요
                .build();

        // RequestDetail 생성 및 추가 (content만 사용)
        RequestDetail detail1 = RequestDetail.builder()
                .content("선택메뉴1")
                .build();

        RequestDetail detail2 = RequestDetail.builder()
                .content("선택메뉴2")
                .build();

        reservation.addRequestDetail(detail1);
        reservation.addRequestDetail(detail2);

        // 2. 저장
        Reservation saved = reservationRepository.save(reservation);
        assertNotNull(saved.getId(), "저장 후 ID가 생성되어야 한다.");

        // 3. 조회
        Optional<Reservation> foundOpt = reservationRepository.findById(saved.getId());
        assertTrue(foundOpt.isPresent(), "저장된 예약이 조회되어야 한다.");

        Reservation found = foundOpt.get();
        assertEquals("RES-001", found.getReservationNumber());
        assertEquals("홍길동", found.getName());
        assertEquals(4, found.getPartySize());
        assertEquals(Reservation.Status.REQUESTED, found.getStatus());
        assertEquals(20, found.getPredictedWait());
        assertNull(found.getMember());
        assertNull(found.getRestaurant());

        // RequestDetail 검증
        assertNotNull(found.getRequestDetails());
        assertEquals(2, found.getRequestDetails().size());
        assertEquals("선택메뉴1", found.getRequestDetails().get(0).getContent());
        assertEquals("선택메뉴2", found.getRequestDetails().get(1).getContent());
    }
}
