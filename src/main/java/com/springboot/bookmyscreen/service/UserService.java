package com.springboot.bookmyscreen.service;

import com.springboot.bookmyscreen.dto.UserSignupRequest;
import com.springboot.bookmyscreen.dto.UserSignupResponse;
import com.springboot.bookmyscreen.entity.UserEntity;
import com.springboot.bookmyscreen.exception.DuplicateResourceException;
import com.springboot.bookmyscreen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public UserSignupResponse signup(UserSignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }
        
        UserEntity userEntity = new UserEntity();
        userEntity.setName(request.getName());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole("USER");
        userRepository.save(userEntity);

        String token = jwtService.issue(request.getEmail());
        
        return new UserSignupResponse(token, request.getName(), request.getEmail(), "USER");
    }
}
