package com.springboot.bookmyscreen.service;

import com.springboot.bookmyscreen.dto.CreateMovieRequest;
import com.springboot.bookmyscreen.dto.CreateMovieResponse;
import com.springboot.bookmyscreen.entity.GenreEntity;
import com.springboot.bookmyscreen.entity.MovieEntity;
import com.springboot.bookmyscreen.exception.FileOperationException;
import com.springboot.bookmyscreen.exception.ResourceNotFoundException;
import com.springboot.bookmyscreen.repository.GenreRepository;
import com.springboot.bookmyscreen.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private FileService fileService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public CreateMovieResponse createMovie(CreateMovieRequest request) {
        // Upload poster file
        String posterFilename;
        try {
            posterFilename = fileService.uploadPoster(request.getPoster());
        } catch (IOException e) {
            throw new FileOperationException("Failed to upload poster file", e);
        }

        // Fetch genres by IDs
        List<GenreEntity> genres = genreRepository.findByIdIn(request.getGenres());

        // Create movie entity
        MovieEntity movie = new MovieEntity();
        movie.setName(request.getName());
        movie.setDescription(request.getDescription());
        movie.setPoster(posterFilename);
        movie.setDuration(request.getDuration());
        movie.setGenres(Set.copyOf(genres));

        // Save movie
        MovieEntity savedMovie = movieRepository.save(movie);

        // Build response with full poster URL
        return CreateMovieResponse.builder()
                .id(savedMovie.getId())
                .name(savedMovie.getName())
                .description(savedMovie.getDescription())
                .poster(baseUrl + "/uploads/posters/" + savedMovie.getPoster())
                .duration(savedMovie.getDuration())
                .genres(savedMovie.getGenres().stream()
                        .map(genre -> CreateMovieResponse.GenreResponse.builder()
                                .id(genre.getId())
                                .name(genre.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .createdAt(LocalDateTime.now())
                .message("Movie created successfully!")
                .build();
    }

    public String deleteMovie(int id) {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        movieRepository.delete(movie);
        return "Movie deleted successfully!";
    }

    public Page<CreateMovieResponse> getAllMovies(Pageable pageable) {
        Page<MovieEntity> moviePage = movieRepository.findAll(pageable);
        
        return moviePage.map(movie -> CreateMovieResponse.builder()
                .id(movie.getId())
                .name(movie.getName())
                .description(movie.getDescription())
                .poster(baseUrl + "/uploads/posters/" + movie.getPoster())
                .duration(movie.getDuration())
                .genres(movie.getGenres().stream()
                        .map(genre -> CreateMovieResponse.GenreResponse.builder()
                                .id(genre.getId())
                                .name(genre.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .createdAt(LocalDateTime.now())
                .message("Movie retrieved successfully!")
                .build());
    }

    public CreateMovieResponse getMovieById(int id) {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        return CreateMovieResponse.builder()
                .id(movie.getId())
                .name(movie.getName())
                .description(movie.getDescription())
                .poster(baseUrl + "/uploads/posters/" + movie.getPoster())
                .duration(movie.getDuration())
                .genres(movie.getGenres().stream()
                        .map(genre -> CreateMovieResponse.GenreResponse.builder()
                                .id(genre.getId())
                                .name(genre.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .createdAt(LocalDateTime.now())
                .message("Movie retrieved successfully!")
                .build();
    }
}
