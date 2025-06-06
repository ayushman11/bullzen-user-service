package com.bullzen.user.controller;

import com.bullzen.user.dto.*;
import com.bullzen.user.entities.User;
import com.bullzen.user.entities.UserRole;
import com.bullzen.user.repository.UserRepository;
import com.bullzen.user.repository.UserRoleRepository;
import com.bullzen.user.service.KafkaProducerService;
import com.bullzen.user.service.UserDetailsServiceImpl;
import com.bullzen.user.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @GetMapping("/health-check")
    public ResponseEntity<ApiResponse<Object>> healthCheck() {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Server is running",
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()
        ));
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsernameOrEmail());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "user signed-in successfully!", jwt), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> signup(@Valid @RequestBody SignUpDto signUpDto) {

        if(userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Username is already taken", null), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Email ID already exists", null), HttpStatus.BAD_REQUEST);
        }
        Optional<UserRole> role = userRoleRepository.findByName("ROLE_TRADER");

        User newUser = User
                .builder()
                .username(signUpDto.getUsername())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .roles(role.map(Collections::singleton).orElse(Collections.emptySet()))
                .build();
        userRepository.save(newUser);
        kafkaProducerService.sendMessage(new UserCreatedDto(newUser));

        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User created successfully",
                null
        );
        return ResponseEntity.ok(response);
    }
}
