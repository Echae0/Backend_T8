package com.t8.backend.t8.backend;

import com.t8.backend.t8.backend.entity.Reservation;
import com.t8.backend.t8.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
@SpringBootTest
@ActiveProfiles("prod")
public class ReservationEntityTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @Rollback(value = false)
    public void testSaveAndFindReservation() {
        // 1. Reservation 엔티티 생성 (member, restaurant는 null로 처리)
        Reservation reservation = Reservation.builder()
                .reservationNumber("RES-001")
                .partySize(4)
                .reservedAt(LocalDateTime.now())
                .joinedAt(null)
                .predictedWait(20)
                .status(Reservation.Status.REQUESTED)
                .member(null)     // 실제 연관관계 테스트 시 Member 엔티티 필요
                .restaurant(null) // 실제 연관관계 테스트 시 Restaurant 엔티티 필요
                .build();

        // 2. 저장
        Reservation saved = reservationRepository.save(reservation);
        assertNotNull(saved.getId(), "저장 후 ID가 생성되어야 한다.");

        // 3. 조회
        Optional<Reservation> foundOpt = reservationRepository.findById(saved.getId());
        assertTrue(foundOpt.isPresent(), "저장된 예약이 조회되어야 한다.");

        Reservation found = foundOpt.get();
        assertEquals("RES-001", found.getReservationNumber());
        assertEquals(4, found.getPartySize());
        assertEquals(Reservation.Status.REQUESTED, found.getStatus());
        assertEquals(20, found.getPredictedWait());
        assertNull(found.getMember());
        assertNull(found.getRestaurant());
    }
}
