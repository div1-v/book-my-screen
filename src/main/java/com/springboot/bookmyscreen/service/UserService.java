package com.springboot.bookmyscreen.service;

import com.springboot.bookmyscreen.dto.UserLoginRequest;
import com.springboot.bookmyscreen.dto.UserLoginResponse;
import com.springboot.bookmyscreen.dto.UserSignupRequest;
import com.springboot.bookmyscreen.dto.UserSignupResponse;
import com.springboot.bookmyscreen.entity.UserEntity;
import com.springboot.bookmyscreen.repository.UserRepository;
import com.springboot.bookmyscreen.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String ENCRYPTION_KEY = "1234567890123456";

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public UserSignupResponse signup(UserSignupRequest request){
        boolean isPresent = userRepository.findByEmail(request.getEmail()).isPresent();
        if(isPresent){
            throw new RuntimeException("User already exists");
        }
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        String token = jwtUtil.generateToken(request.getEmail());
        UserSignupResponse userSignupResponse = new UserSignupResponse();
        userSignupResponse.setToken(token);
        userSignupResponse.setName(user.getName());
        userSignupResponse.setEmail(user.getEmail());
        userSignupResponse.setRole(user.getRole());
        return userSignupResponse;

    }

    public UserLoginResponse login(UserLoginRequest request){
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User does not exist"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        UserLoginResponse response = new UserLoginResponse();
        response.setToken(token);
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
