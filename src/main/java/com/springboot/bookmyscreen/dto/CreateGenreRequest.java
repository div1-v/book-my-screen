package com.springboot.bookmyscreen.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGenreRequest {

    @NotBlank(message = "Genre name is required")
    private String name;
} 