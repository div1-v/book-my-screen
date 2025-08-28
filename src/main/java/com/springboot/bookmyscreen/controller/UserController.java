package com.springboot.bookmyscreen.controller;

import com.springboot.bookmyscreen.dto.UserLoginRequest;
import com.springboot.bookmyscreen.dto.UserLoginResponse;
import com.springboot.bookmyscreen.dto.UserSignupRequest;
import com.springboot.bookmyscreen.dto.UserSignupResponse;
import com.springboot.bookmyscreen.repository.UserRepository;
import com.springboot.bookmyscreen.service.UserService;
import com.springboot.bookmyscreen.util.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupRequest request){
        UserSignupResponse userSignupResponse= userService.signup(request);
        return ResponseEntity.ok(userSignupResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        return ResponseEntity.ok(jwtUtil.generateToken(request.getEmail()));
    }
}
