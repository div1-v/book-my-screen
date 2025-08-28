package com.springboot.bookmyscreen.service;

import com.springboot.bookmyscreen.dto.CreateReservationRequest;
import com.springboot.bookmyscreen.dto.CreateReservationResponse;
import com.springboot.bookmyscreen.dto.SeatResponse;
import com.springboot.bookmyscreen.entity.*;
import com.springboot.bookmyscreen.enums.ReservationStatus;
import com.springboot.bookmyscreen.exception.DuplicateResourceException;
import com.springboot.bookmyscreen.exception.InvalidRequestException;
import com.springboot.bookmyscreen.exception.ResourceNotFoundException;
import com.springboot.bookmyscreen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatRepository seatRepository;

    /**
     * Create a reservation with proper locking to prevent double booking
     * Uses pessimistic locking to ensure thread safety
     */
    public CreateReservationResponse createReservation(CreateReservationRequest request) {
        // Validate show exists and is in the future
        ShowTimeEntity show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", request.getShowId()));

        if (show.getStart_time().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Cannot reserve seats for a show that has already started");
        }

        // Validate user exists
        UserEntity user = userRepository.findById(request.getUserId().longValue())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        // Validate seats exist and belong to the show's auditorium
        List<SeatEntity> seats = seatRepository.findAllById(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new ResourceNotFoundException("One or more seats not found");
        }

        // Check if all seats belong to the same auditorium as the show
        for (SeatEntity seat : seats) {
            if (seat.getAuditorium().getId() != show.getAuditorium().getId()) {
                throw new InvalidRequestException("Seat " + seat.getId() + " does not belong to the show's auditorium");
            }
        }

        // Check for existing reservations for these seats (with locking)
        List<ReservationEntity> existingReservations = new ArrayList<>();
        for (Integer seatId : request.getSeatIds()) {
            reservationRepository.findReservationByShowAndSeatWithLock(request.getShowId(), seatId)
                    .ifPresent(existingReservations::add);
        }

        if (!existingReservations.isEmpty()) {
            List<Integer> reservedSeatIds = existingReservations.stream()
                    .map(r -> r.getSeat().getId())
                    .collect(Collectors.toList());
            throw new DuplicateResourceException("Seats " + reservedSeatIds + " are already reserved for this show");
        }

        // Check if user has already reserved any of these seats
        List<ReservationEntity> userReservations = reservationRepository.findUserReservationsForSeats(
                request.getShowId(), request.getUserId(), request.getSeatIds());
        
        if (!userReservations.isEmpty()) {
            List<Integer> userReservedSeatIds = userReservations.stream()
                    .map(r -> r.getSeat().getId())
                    .collect(Collectors.toList());
            throw new DuplicateResourceException("User has already reserved seats " + userReservedSeatIds + " for this show");
        }

        // Create reservations for all seats
        List<ReservationEntity> reservations = new ArrayList<>();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (SeatEntity seat : seats) {
            ReservationEntity reservation = new ReservationEntity();
            reservation.setUser(user);
            reservation.setShowtime(show);
            reservation.setSeat(seat);
            reservation.setStatus(ReservationStatus.BOOKED);
            reservation.setCreatedAt(now);
            reservations.add(reservation);
        }

        // Save all reservations atomically
        List<ReservationEntity> savedReservations = reservationRepository.saveAll(reservations);

        // Build response
        return CreateReservationResponse.builder()
                .id(savedReservations.get(0).getId().intValue())
                .show(CreateReservationResponse.ShowInfo.builder()
                        .id(show.getId())
                        .movieName(show.getMovie().getName())
                        .auditoriumName(show.getAuditorium().getName())
                        .startTime(show.getStart_time())
                        .endTime(show.getEnd_time())
                        .price(show.getPrice())
                        .build())
                .user(CreateReservationResponse.UserInfo.builder()
                        .id(user.getId().intValue())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .seats(seats.stream()
                        .map(seat -> CreateReservationResponse.SeatInfo.builder()
                                .id(seat.getId())
                                .seatNumber(seat.getSeat_number())
                                .auditoriumName(seat.getAuditorium().getName())
                                .build())
                        .collect(Collectors.toList()))
                .status(ReservationStatus.BOOKED.name())
                .createdAt(LocalDateTime.now())
                .message("Reservation created successfully for " + seats.size() + " seat(s)!")
                .build();
    }

    /**
     * Get available seats for a show
     */
    public List<SeatResponse> getAvailableSeatsForShow(Integer showId) {
        // Validate show exists
        ShowTimeEntity show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", showId));

        List<SeatEntity> availableSeats = reservationRepository.findAvailableSeatsForShow(showId);
        
        return availableSeats.stream()
                .map(seat -> SeatResponse.builder()
                        .id(seat.getId())
                        .seatNumber(seat.getSeat_number())
                        .auditoriumId(seat.getAuditorium().getId())
                        .auditoriumName(seat.getAuditorium().getName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get reservations by user ID
     */
    public List<CreateReservationResponse> getUserReservations(Integer userId) {
        // Validate user exists
        userRepository.findById(userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<ReservationEntity> reservations = reservationRepository.findByUserId(userId);
        
        return reservations.stream()
                .map(this::mapToReservationResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cancel a reservation
     */
    public String cancelReservation(Long reservationId, Integer userId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

        // Check if user owns this reservation
        if (!reservation.getUser().getId().equals(userId)) {
            throw new InvalidRequestException("You can only cancel your own reservations");
        }

        // Check if show has already started
        if (reservation.getShowtime().getStart_time().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Cannot cancel reservation for a show that has already started");
        }

        // Check if reservation is already cancelled
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new InvalidRequestException("Reservation is already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        return "Reservation cancelled successfully!";
    }

    /**
     * Helper method to map ReservationEntity to CreateReservationResponse
     */
    private CreateReservationResponse mapToReservationResponse(ReservationEntity reservation) {
        return CreateReservationResponse.builder()
                .id(reservation.getId().intValue())
                .show(CreateReservationResponse.ShowInfo.builder()
                        .id(reservation.getShowtime().getId())
                        .movieName(reservation.getShowtime().getMovie().getName())
                        .auditoriumName(reservation.getShowtime().getAuditorium().getName())
                        .startTime(reservation.getShowtime().getStart_time())
                        .endTime(reservation.getShowtime().getEnd_time())
                        .price(reservation.getShowtime().getPrice())
                        .build())
                .user(CreateReservationResponse.UserInfo.builder()
                        .id(reservation.getUser().getId().intValue())
                        .name(reservation.getUser().getName())
                        .email(reservation.getUser().getEmail())
                        .build())
                .seats(List.of(CreateReservationResponse.SeatInfo.builder()
                        .id(reservation.getSeat().getId())
                        .seatNumber(reservation.getSeat().getSeat_number())
                        .auditoriumName(reservation.getSeat().getAuditorium().getName())
                        .build()))
                .status(reservation.getStatus().name())
                .createdAt(reservation.getCreatedAt().toLocalDateTime())
                .message("Reservation retrieved successfully!")
                .build();
    }
}