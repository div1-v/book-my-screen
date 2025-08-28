package com.springboot.bookmyscreen.service;

import com.springboot.bookmyscreen.dto.CreateShowRequest;
import com.springboot.bookmyscreen.dto.CreateShowResponse;
import com.springboot.bookmyscreen.entity.AuditoriumEntity;
import com.springboot.bookmyscreen.entity.MovieEntity;
import com.springboot.bookmyscreen.entity.ShowTimeEntity;
import com.springboot.bookmyscreen.exception.InvalidRequestException;
import com.springboot.bookmyscreen.exception.ResourceNotFoundException;
import com.springboot.bookmyscreen.repository.AuditoriumRepository;
import com.springboot.bookmyscreen.repository.MovieRepository;
import com.springboot.bookmyscreen.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public CreateShowResponse createShow(CreateShowRequest request) {
        // Validate movie exists
        MovieEntity movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", request.getMovieId()));

        // Validate auditorium exists
        AuditoriumEntity auditorium = auditoriumRepository.findById(request.getAuditoriumId())
                .orElseThrow(() -> new ResourceNotFoundException("Auditorium", "id", request.getAuditoriumId()));

        // Validate time constraints
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidRequestException("Start time cannot be after end time");
        }

        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Start time cannot be in the past");
        }

        // Create show entity
        ShowTimeEntity show = new ShowTimeEntity();
        show.setMovie(movie);
        show.setAuditorium(auditorium);
        show.setStart_time(request.getStartTime());
        show.setEnd_time(request.getEndTime());
        show.setPrice(request.getPrice());

        // Save show
        ShowTimeEntity savedShow = showRepository.save(show);

        // Build response
        return CreateShowResponse.builder()
                .id(savedShow.getId())
                .movie(CreateShowResponse.MovieInfo.builder()
                        .id(movie.getId())
                        .name(movie.getName())
                        .poster(baseUrl + "/uploads/posters/" + movie.getPoster())
                        .build())
                .auditorium(CreateShowResponse.AuditoriumInfo.builder()
                        .id(auditorium.getId())
                        .name(auditorium.getName())
                        .capacity(auditorium.getCapacity())
                        .build())
                .startTime(savedShow.getStart_time())
                .endTime(savedShow.getEnd_time())
                .price(savedShow.getPrice())
                .createdAt(LocalDateTime.now())
                .message("Show created successfully!")
                .build();
    }

    public String deleteShow(int id) {
        ShowTimeEntity show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", id));
        
        showRepository.delete(show);
        return "Show deleted successfully!";
    }

    public Page<CreateShowResponse> getAllShows(Pageable pageable) {
        Page<ShowTimeEntity> showPage = showRepository.findAll(pageable);
        
        return showPage.map(show -> CreateShowResponse.builder()
                .id(show.getId())
                .movie(CreateShowResponse.MovieInfo.builder()
                        .id(show.getMovie().getId())
                        .name(show.getMovie().getName())
                        .poster(baseUrl + "/uploads/posters/" + show.getMovie().getPoster())
                        .build())
                .auditorium(CreateShowResponse.AuditoriumInfo.builder()
                        .id(show.getAuditorium().getId())
                        .name(show.getAuditorium().getName())
                        .capacity(show.getAuditorium().getCapacity())
                        .build())
                .startTime(show.getStart_time())
                .endTime(show.getEnd_time())
                .price(show.getPrice())
                .createdAt(LocalDateTime.now())
                .message("Show retrieved successfully!")
                .build());
    }

    public CreateShowResponse getShowById(int id) {
        ShowTimeEntity show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", id));
        
        return CreateShowResponse.builder()
                .id(show.getId())
                .movie(CreateShowResponse.MovieInfo.builder()
                        .id(show.getMovie().getId())
                        .name(show.getMovie().getName())
                        .poster(baseUrl + "/uploads/posters/" + show.getMovie().getPoster())
                        .build())
                .auditorium(CreateShowResponse.AuditoriumInfo.builder()
                        .id(show.getAuditorium().getId())
                        .name(show.getAuditorium().getName())
                        .capacity(show.getAuditorium().getCapacity())
                        .build())
                .startTime(show.getStart_time())
                .endTime(show.getEnd_time())
                .price(show.getPrice())
                .createdAt(LocalDateTime.now())
                .message("Show retrieved successfully!")
                .build();
    }
} 