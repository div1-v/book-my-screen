package com.springboot.bookmyscreen.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateMovieRequest {

    @NotBlank(message = "Movie name is required")
    private String name;

    private String description;

    @NotNull(message = "Poster image is required")
    private MultipartFile poster;

    @Min(value = 1, message = "Duration must be at least 1 second")
    private int duration; // duration in seconds

    @NotNull(message = "At least one genre must be selected")
    private List<Integer> genres; // IDs of selected genres

}
