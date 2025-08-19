package com.springboot.bookmyscreen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auditoriums")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriumEntity {

    @Id
    private int id;

    private String name;
    private int capacity;
}
