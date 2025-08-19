package com.springboot.bookmyscreen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignupRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3,message = "Name should be at least 5 characters")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
