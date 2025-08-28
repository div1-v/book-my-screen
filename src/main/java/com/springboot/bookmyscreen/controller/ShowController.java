package com.springboot.bookmyscreen.controller;

import com.springboot.bookmyscreen.dto.CreateShowRequest;
import com.springboot.bookmyscreen.dto.CreateShowResponse;
import com.springboot.bookmyscreen.service.ShowService;
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
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateShowResponse> createShow(@Valid @RequestBody CreateShowRequest request) {
        CreateShowResponse showResponse = showService.createShow(request);
        return ResponseEntity.ok(showResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteShow(@PathVariable("id") int id) {
        String response = showService.deleteShow(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> listShows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CreateShowResponse> shows = showService.getAllShows(pageable);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShowById(@PathVariable("id") int id) {
        CreateShowResponse show = showService.getShowById(id);
        return ResponseEntity.ok(show);
    }
} 