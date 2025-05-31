package com.bullzen.user.controller;

import com.bullzen.user.dto.ApiResponse;
import com.bullzen.user.entities.User;
import com.bullzen.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
        userService.saveUser(user);

        ApiResponse<User> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User created successfully",
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> usersList = userService.getAllUsers();

        ApiResponse<List<User>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Users fetched successfully",
                usersList
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
