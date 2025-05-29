package com.t8.backend.t8.backend.entity;

import com.t8.backend.t8.backend.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB 사용 시
@ActiveProfiles("prod") // application-test.properties 로드
@Transactional
@Rollback(true)
public class MemberEntityTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional // 테스트 후 데이터 롤백을 위해 추가
    @Rollback(true) // 테스트가 끝나도 롤백하지 않도록 설정

    void testSaveMember() {
        // 1. Member 엔티티 생성
        Member member = Member.builder()
                .memberNumber("MEMBER-001")
                .name("김철수")
                .email("kimchulsoo@example.com")
                .phoneNumber("010-1234-5678")
                .address("서울시 강남구")
                .birthDate(LocalDate.of(1990, 5, 15))
                .status(Member.Status.ACTIVE) // Member.Status로 명시적으로 참조
                .maxReservationCount(5)
                .noshowCounts(0)
                .build();

        // 2. Member 저장
        Member savedMember = memberRepository.save(member);

        // 3. 저장된 Member가 null이 아니고 ID가 할당되었는지 확인
        assertNotNull(savedMember.getId());
        assertEquals("MEMBER-001", savedMember.getMemberNumber());
        assertEquals("김철수", savedMember.getName());
        assertEquals("kimchulsoo@example.com", savedMember.getEmail());

        // 4. 저장된 Member를 다시 조회하여 확인
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());
        assertTrue(foundMember.isPresent(), "저장된 멤버를 찾을 수 없습니다.");
        assertEquals(savedMember.getEmail(), foundMember.get().getEmail());
        assertEquals(Member.Status.ACTIVE, foundMember.get().getStatus());

        // 5. Auditing 필드 (BaseEntity 상속)가 잘 설정되었는지 확인
        assertNotNull(foundMember.get().getCreatedAt());
        assertNotNull(foundMember.get().getUpdatedAt());
    }
}