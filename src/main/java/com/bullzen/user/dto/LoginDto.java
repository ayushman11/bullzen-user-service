package com.bullzen.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank(message = "username or email is required")
    private String usernameOrEmail;

    @NotBlank(message = "password is required")
    private String password;
}
