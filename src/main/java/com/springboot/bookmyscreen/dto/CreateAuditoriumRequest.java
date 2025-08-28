package com.springboot.bookmyscreen.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuditoriumRequest {

    @NotBlank(message = "Auditorium name is required")
    private String name;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;
} 