package com.t8.backend.t8.backend.entity;

import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.repository.ReservationRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase; // 제거
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;       // 제거
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest; // ⭐ @SpringBootTest로 변경
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest // ⭐ @DataJpaTest 대신 @SpringBootTest 사용
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 여전히 실제 DB 사용을 원한다면 유지
@ActiveProfiles("prod")
public class ReservationEntityTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private ReservationRepository reservationRepository;

    @Test
    @Transactional // ⭐ @SpringBootTest를 사용할 때는 Transactional 어노테이션을 추가하여 테스트 후 롤백되도록 하는 것이 일반적입니다.
    void createReservationWithDetails() {
        // 1. Member & Restaurant 저장
        Member member = memberRepository.save(Member.builder()
                .name("홍길동").email("hong@test.com").build());

        Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
                .restaurantName("김밥천국").location("서울").dailyLimitedTeams(10)
                .availableTeams(3).build());

        // 2. 예약 생성
        Reservation reservation = Reservation.builder()
                .reservationNumber("RES1234")
                .partySize(4)
                .reservedAt(LocalDateTime.now())
                .joinedAt(LocalDateTime.now().plusMinutes(10))
                .predictedWait(15)
                .member(member)
                .restaurant(restaurant)
                .build();

        // 4. 저장
        reservationRepository.save(reservation);

        // 5. 검증
        Optional<Reservation> result = reservationRepository.findById(reservation.getId());
        assertTrue(result.isPresent());
//        assertEquals(1, result.get().getRequestDetails().size());
    }
}