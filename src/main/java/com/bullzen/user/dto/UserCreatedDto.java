package com.bullzen.user.dto;

import com.bullzen.user.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreatedDto {

    @NotNull
    private Long userId;

    @NotBlank
    private String username;

    public UserCreatedDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
    }
}
