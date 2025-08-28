package com.springboot.bookmyscreen.service;

import com.springboot.bookmyscreen.dto.CreateGenreRequest;
import com.springboot.bookmyscreen.dto.CreateGenreResponse;
import com.springboot.bookmyscreen.entity.GenreEntity;
import com.springboot.bookmyscreen.exception.DuplicateResourceException;
import com.springboot.bookmyscreen.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public CreateGenreResponse createGenre(CreateGenreRequest request) {
        // Check if genre already exists
        if (genreRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new DuplicateResourceException("Genre", "name", request.getName());
        }

        // Create genre entity
        GenreEntity genre = new GenreEntity();
        genre.setName(request.getName().toUpperCase()); // Store genres in uppercase

        // Save genre
        GenreEntity savedGenre = genreRepository.save(genre);

        // Build response
        return CreateGenreResponse.builder()
                .id(savedGenre.getId())
                .name(savedGenre.getName())
                .createdAt(LocalDateTime.now())
                .message("Genre created successfully!")
                .build();
    }
} 