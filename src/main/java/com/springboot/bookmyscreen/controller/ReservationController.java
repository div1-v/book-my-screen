package com.springboot.bookmyscreen.controller;

import com.springboot.bookmyscreen.dto.CreateReservationRequest;
import com.springboot.bookmyscreen.dto.CreateReservationResponse;
import com.springboot.bookmyscreen.dto.SeatResponse;
import com.springboot.bookmyscreen.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CreateReservationResponse> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        CreateReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-seats/{showId}")
    public ResponseEntity<List<SeatResponse>> getAvailableSeatsForShow(@PathVariable("showId") Integer showId) {
        List<SeatResponse> availableSeats = reservationService.getAvailableSeatsForShow(showId);
        return ResponseEntity.ok(availableSeats);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CreateReservationResponse>> getUserReservations(@PathVariable("userId") Integer userId) {
        List<CreateReservationResponse> reservations = reservationService.getUserReservations(userId);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> cancelReservation(
            @PathVariable("reservationId") Long reservationId,
            @RequestParam("userId") Integer userId) {
        String message = reservationService.cancelReservation(reservationId, userId);
        return ResponseEntity.ok(message);
    }
}