package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 필요 시 커스텀 쿼리 작성
}