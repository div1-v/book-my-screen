package com.springboot.bookmyscreen.repository;

import com.springboot.bookmyscreen.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Integer> {
    
    List<SeatEntity> findByAuditoriumId(int auditoriumId);
    
    @Modifying
    @Query("DELETE FROM SeatEntity s WHERE s.auditorium.id = :auditoriumId")
    void deleteByAuditoriumId(@Param("auditoriumId") int auditoriumId);
} 