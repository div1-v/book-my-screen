package com.springboot.bookmyscreen.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShowRequest {

    @NotNull(message = "Movie ID is required")
    private Integer movieId;

    @NotNull(message = "Auditorium ID is required")
    private Integer auditoriumId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Min(value = 1, message = "Price must be at least 1")
    private int price;
} 