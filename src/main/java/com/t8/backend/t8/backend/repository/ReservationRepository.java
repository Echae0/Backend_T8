package com.t8.backend.t8.backend.repository;

import com.t8.backend.t8.backend.entity.Reservation;
import com.t8.backend.t8.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.restaurant.id = :restaurantId AND r.status <> 'JOINED'")
    List<Reservation> findActiveByRestaurantId(Long restaurantId);

//    List<Reservation> findByRestaurantId(Long restaurantId);
    List<Reservation> findByMemberId(Long memberId);
    int countByRestaurantAndStatus(Restaurant restaurant, Reservation.Status status);
    int countByRestaurantAndStatusIn(Restaurant restaurant, List<Reservation.Status> statuses);


}