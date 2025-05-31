package com.bullzen.user.controller;

import com.bullzen.user.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> healthCheck() {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Server is running",
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
