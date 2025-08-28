package com.springboot.bookmyscreen.repository;

import com.springboot.bookmyscreen.entity.AuditoriumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriumRepository extends JpaRepository<AuditoriumEntity, Integer> {
} 