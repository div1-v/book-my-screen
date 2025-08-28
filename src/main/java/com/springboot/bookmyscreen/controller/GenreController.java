package com.springboot.bookmyscreen.controller;

import com.springboot.bookmyscreen.dto.CreateGenreRequest;
import com.springboot.bookmyscreen.dto.CreateGenreResponse;
import com.springboot.bookmyscreen.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateGenreResponse> createGenre(@Valid @RequestBody CreateGenreRequest request) {
        CreateGenreResponse genreResponse = genreService.createGenre(request);
        return ResponseEntity.ok(genreResponse);
    }
} 