package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Reservation;
import com.t8.backend.t8.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.restaurant.id = :restaurantId AND r.status <> 'JOINED'")
    List<Reservation> findActiveByRestaurantId(Long restaurantId);

//    List<Reservation> findByRestaurantId(Long restaurantId);
    List<Reservation> findByMemberId(Long memberId);
    List<Reservation> findByRestaurantAndStatusIn(Restaurant restaurant, List<Reservation.Status> statuses);
    int countByRestaurantAndStatusIn(Restaurant restaurant, List<Reservation.Status> statuses);
//    int countByRestaurantAndDateAndStatusIn(Restaurant restaurant, LocalDateTime reservedAt, List<Reservation.Status> statuses);
    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.restaurant = :restaurant " +
            "AND FUNCTION('DATE', r.reservedAt) = FUNCTION('DATE', :reservedAt) " +
            "AND r.status IN :statuses")
    int countByRestaurantAndReservedAtDateAndStatusIn(
            @Param("restaurant") Restaurant restaurant,
            @Param("reservedAt") LocalDateTime reservedAt,
            @Param("statuses") List<Reservation.Status> statuses);


}