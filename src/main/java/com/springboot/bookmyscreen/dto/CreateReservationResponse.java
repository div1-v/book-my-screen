package com.springboot.bookmyscreen.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CreateReservationResponse {
    private Integer id;
    private ShowInfo show;
    private UserInfo user;
    private List<SeatInfo> seats;
    private String status;
    private LocalDateTime createdAt;
    private String message;
    
    @Data
    @Builder
    public static class ShowInfo {
        private Integer id;
        private String movieName;
        private String auditoriumName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer price;
    }
    
    @Data
    @Builder
    public static class UserInfo {
        private Integer id;
        private String name;
        private String email;
    }
    
    @Data
    @Builder
    public static class SeatInfo {
        private Integer id;
        private Integer seatNumber;
        private String auditoriumName;
    }
} 