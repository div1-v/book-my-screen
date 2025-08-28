package com.springboot.bookmyscreen.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateReservationRequest {
    
    @NotNull(message = "Show ID is required")
    private Integer showId;
    
    @NotEmpty(message = "At least one seat must be selected")
    private List<Integer> seatIds;
    
    @NotNull(message = "User ID is required")
    private Integer userId;
} 