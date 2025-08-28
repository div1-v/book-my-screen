package com.springboot.bookmyscreen.repository;

import com.springboot.bookmyscreen.entity.ShowTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<ShowTimeEntity, Integer> {
} 