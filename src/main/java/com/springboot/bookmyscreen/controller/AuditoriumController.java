package com.springboot.bookmyscreen.controller;

import com.springboot.bookmyscreen.dto.CreateAuditoriumRequest;
import com.springboot.bookmyscreen.dto.CreateAuditoriumResponse;
import com.springboot.bookmyscreen.service.AuditoriumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auditoriums")
public class AuditoriumController {

    @Autowired
    private AuditoriumService auditoriumService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateAuditoriumResponse> createAuditorium(@Valid @RequestBody CreateAuditoriumRequest request) {
        CreateAuditoriumResponse auditoriumResponse = auditoriumService.createAuditorium(request);
        return ResponseEntity.ok(auditoriumResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAuditorium(@PathVariable("id") int id) {
        String response = auditoriumService.deleteAuditorium(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> listAuditoriums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CreateAuditoriumResponse> auditoriums = auditoriumService.getAllAuditoriums(pageable);
        return ResponseEntity.ok(auditoriums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuditoriumById(@PathVariable("id") int id) {
        CreateAuditoriumResponse auditorium = auditoriumService.getAuditoriumById(id);
        return ResponseEntity.ok(auditorium);
    }
} 