package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); // 이메일 중복 확인을 위해 추가
    Optional<Member> findById(Long id);
}