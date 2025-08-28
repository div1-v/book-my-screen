package com.springboot.bookmyscreen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieResponse {

    private int id;
    private String name;
    private String description;
    private String poster;
    private int duration;
    private Set<GenreResponse> genres;
    private LocalDateTime createdAt;
    private String message;

    // Nested DTO for genre information
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenreResponse {
        private int id;
        private String name;
    }
}
