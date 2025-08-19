package com.springboot.bookmyscreen.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatEntity {

    @Id
    private int id;

    private int seat_number;
    
    @ManyToOne
    @JoinColumn(name = "auditorium_id", nullable = false)
    private AuditoriumEntity auditorium;
}
