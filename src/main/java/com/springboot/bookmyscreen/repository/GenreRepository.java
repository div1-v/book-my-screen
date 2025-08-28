package com.springboot.bookmyscreen.repository;

import com.springboot.bookmyscreen.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    List<GenreEntity> findByIdIn(List<Integer> ids);
    Optional<GenreEntity> findByNameIgnoreCase(String name);
} 