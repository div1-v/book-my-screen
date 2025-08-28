package com.springboot.bookmyscreen.service;

import com.springboot.bookmyscreen.dto.CreateAuditoriumRequest;
import com.springboot.bookmyscreen.dto.CreateAuditoriumResponse;
import com.springboot.bookmyscreen.entity.AuditoriumEntity;
import com.springboot.bookmyscreen.entity.SeatEntity;
import com.springboot.bookmyscreen.exception.ResourceNotFoundException;
import com.springboot.bookmyscreen.repository.AuditoriumRepository;
import com.springboot.bookmyscreen.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuditoriumService {

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private SeatRepository seatRepository;

    public CreateAuditoriumResponse createAuditorium(CreateAuditoriumRequest request) {
        // Create auditorium entity
        AuditoriumEntity auditorium = new AuditoriumEntity();
        auditorium.setName(request.getName());
        auditorium.setCapacity(request.getCapacity());

        // Save auditorium first to get the ID
        AuditoriumEntity savedAuditorium = auditoriumRepository.save(auditorium);

        // Create seats automatically based on capacity
        createSeatsForAuditorium(savedAuditorium, request.getCapacity());

        // Build response
        return CreateAuditoriumResponse.builder()
                .id(savedAuditorium.getId())
                .name(savedAuditorium.getName())
                .capacity(savedAuditorium.getCapacity())
                .createdAt(LocalDateTime.now())
                .message("Auditorium created successfully with " + request.getCapacity() + " seats!")
                .build();
    }

    private void createSeatsForAuditorium(AuditoriumEntity auditorium, int capacity) {
        List<SeatEntity> seats = new ArrayList<>();
        
        for (int seatNumber = 1; seatNumber <= capacity; seatNumber++) {
            SeatEntity seat = new SeatEntity();
            seat.setSeat_number(seatNumber);
            seat.setAuditorium(auditorium);
            seats.add(seat);
        }
        
        seatRepository.saveAll(seats);
    }

    public String deleteAuditorium(int id) {
        AuditoriumEntity auditorium = auditoriumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auditorium", "id", id));
        
        // Delete associated seats first (due to foreign key constraint)
        seatRepository.deleteByAuditoriumId(id);
        
        // Then delete auditorium
        auditoriumRepository.delete(auditorium);
        return "Auditorium and all associated seats deleted successfully!";
    }

    public Page<CreateAuditoriumResponse> getAllAuditoriums(Pageable pageable) {
        Page<AuditoriumEntity> auditoriumPage = auditoriumRepository.findAll(pageable);
        
        return auditoriumPage.map(auditorium -> CreateAuditoriumResponse.builder()
                .id(auditorium.getId())
                .name(auditorium.getName())
                .capacity(auditorium.getCapacity())
                .createdAt(LocalDateTime.now())
                .message("Auditorium retrieved successfully!")
                .build());
    }

    public CreateAuditoriumResponse getAuditoriumById(int id) {
        AuditoriumEntity auditorium = auditoriumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auditorium", "id", id));
        
        return CreateAuditoriumResponse.builder()
                .id(auditorium.getId())
                .name(auditorium.getName())
                .capacity(auditorium.getCapacity())
                .createdAt(LocalDateTime.now())
                .message("Auditorium retrieved successfully!")
                .build();
    }
} 