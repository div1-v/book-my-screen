package com.springboot.bookmyscreen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowTimeEntity {

    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(name = "auditorium_id", nullable = false)
    private AuditoriumEntity auditorium;

    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private int price;
}
