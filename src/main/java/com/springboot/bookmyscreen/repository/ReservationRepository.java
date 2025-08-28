package com.springboot.bookmyscreen.repository;

import com.springboot.bookmyscreen.entity.ReservationEntity;
import com.springboot.bookmyscreen.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    
    // Check if seats are already reserved for a specific show
    @Query("SELECT r FROM ReservationEntity r WHERE r.showtime.id = :showId AND r.seat.id IN :seatIds AND r.status IN ('BOOKED')")
    List<ReservationEntity> findExistingReservationsForSeats(@Param("showId") Integer showId, @Param("seatIds") List<Integer> seatIds);
    
    // Find reservations by show ID
    @Query("SELECT r FROM ReservationEntity r WHERE r.showtime.id = :showId")
    List<ReservationEntity> findByShowId(@Param("showId") Integer showId);
    
    // Find reservations by user ID
    @Query("SELECT r FROM ReservationEntity r WHERE r.user.id = :userId")
    List<ReservationEntity> findByUserId(@Param("userId") Integer userId);
    
    // Find reservation by show and seat with pessimistic lock to prevent double booking
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservationEntity r WHERE r.showtime.id = :showId AND r.seat.id = :seatId AND r.status IN ('BOOKED')")
    Optional<ReservationEntity> findReservationByShowAndSeatWithLock(@Param("showId") Integer showId, @Param("seatId") Integer seatId);
    
    // Check if user has already reserved any of these seats for this show
    @Query("SELECT r FROM ReservationEntity r WHERE r.showtime.id = :showId AND r.user.id = :userId AND r.seat.id IN :seatIds AND r.status IN ('BOOKED')")
    List<ReservationEntity> findUserReservationsForSeats(@Param("showId") Integer showId, @Param("userId") Integer userId, @Param("seatIds") List<Integer> seatIds);
    
    // Get available seats for a show
    @Query("SELECT s FROM SeatEntity s WHERE s.auditorium.id = (SELECT st.auditorium.id FROM ShowTimeEntity st WHERE st.id = :showId) AND s.id NOT IN (SELECT r.seat.id FROM ReservationEntity r WHERE r.showtime.id = :showId AND r.status IN ('BOOKED'))")
    List<com.springboot.bookmyscreen.entity.SeatEntity> findAvailableSeatsForShow(@Param("showId") Integer showId);
}