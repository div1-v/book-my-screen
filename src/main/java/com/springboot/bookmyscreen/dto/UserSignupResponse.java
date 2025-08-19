package com.springboot.bookmyscreen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupResponse {
    private String token;
    private String name;
    private String email;
    private String role;
}
