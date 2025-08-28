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
public class CreateAuditoriumResponse {

    private int id;
    private String name;
    private int capacity;
    private LocalDateTime createdAt;
    private String message;
} 