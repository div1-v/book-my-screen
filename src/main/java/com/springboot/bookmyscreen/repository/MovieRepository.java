package com.springboot.bookmyscreen.repository;

import com.springboot.bookmyscreen.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {

}
