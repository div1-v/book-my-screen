package com.springboot.bookmyscreen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGenreResponse {

    private int id;
    private String name;
    private LocalDateTime createdAt;
    private String message;
} 