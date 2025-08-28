package com.springboot.bookmyscreen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {

    private int id;
    private int seatNumber;
    private int auditoriumId;
    private String auditoriumName;
} 