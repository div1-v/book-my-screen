package com.springboot.bookmyscreen.controller;

import com.springboot.bookmyscreen.dto.SeatResponse;
import com.springboot.bookmyscreen.entity.SeatEntity;
import com.springboot.bookmyscreen.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    @GetMapping("/auditorium/{auditoriumId}")
    public ResponseEntity<List<SeatResponse>> getSeatsByAuditorium(@PathVariable("auditoriumId") int auditoriumId) {
        try {
            List<SeatEntity> seats = seatRepository.findByAuditoriumId(auditoriumId);
            
            List<SeatResponse> seatResponses = seats.stream()
                    .map(seat -> SeatResponse.builder()
                            .id(seat.getId())
                            .seatNumber(seat.getSeat_number())
                            .auditoriumId(seat.getAuditorium().getId())
                            .auditoriumName(seat.getAuditorium().getName())
                            .build())
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(seatResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 