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
public class CreateShowResponse {

    private int id;
    private MovieInfo movie;
    private AuditoriumInfo auditorium;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;
    private LocalDateTime createdAt;
    private String message;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieInfo {
        private int id;
        private String name;
        private String poster;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditoriumInfo {
        private int id;
        private String name;
        private int capacity;
    }
} 