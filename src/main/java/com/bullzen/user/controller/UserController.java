package com.bullzen.user.controller;

import com.bullzen.user.dto.ApiResponse;
import com.bullzen.user.entities.User;
import com.bullzen.user.entities.UserRole;
import com.bullzen.user.repository.UserRoleRepository;
import com.bullzen.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> usersList = userService.getAllUsers();

        ApiResponse<List<User>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Users fetched successfully",
                usersList
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUserByUsername(username);

        ApiResponse<User> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User fetched successfully",
                user
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{usernameOrEmail}/roles")
    public ResponseEntity<ApiResponse<User>> addUserRolesToUser(@PathVariable String usernameOrEmail, @RequestBody List<String> userRoles) {
        User user = userService.getByUsernameOrEmail(usernameOrEmail);

        Set<UserRole> currentRoles = user.getRoles();
        Set<UserRole> newRoles = userRoles.stream()
                .map(roleName -> userRoleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        currentRoles.addAll(newRoles);
        user.setRoles(currentRoles);

        userService.saveUser(user);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "roles updated successfully", user), HttpStatus.OK);
    }
}
