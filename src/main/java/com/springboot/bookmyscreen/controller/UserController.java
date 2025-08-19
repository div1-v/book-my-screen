package com.springboot.bookmyscreen.controller;

import com.springboot.bookmyscreen.dto.UserLoginRequest;
import com.springboot.bookmyscreen.dto.UserLoginResponse;
import com.springboot.bookmyscreen.dto.UserSignupRequest;
import com.springboot.bookmyscreen.dto.UserSignupResponse;
import com.springboot.bookmyscreen.repository.UserRepository;
import com.springboot.bookmyscreen.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupRequest request){
        UserSignupResponse signupResponse =  userService.signup(request);
        return ResponseEntity.ok(signupResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request){
        System.out.println(request.getEmail()+"  "+request.getPassword());
        UserLoginResponse loginResponse =  userService.login(request);
        return ResponseEntity.ok(loginResponse);
    }
}
