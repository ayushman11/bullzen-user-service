package com.bullzen.user.service;

import com.bullzen.user.dto.UserDto;
import com.bullzen.user.entities.User;
import com.bullzen.user.entities.UserRole;
import com.bullzen.user.repository.UserRepository;
import com.bullzen.user.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long Id) {
        return userRepository.findById(Id);
    }

    public void deleteUser(Long Id) {
        userRepository.deleteById(Id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User convertToEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Set<UserRole> roleEntities = dto.getRoles().stream()
                .map(roleName -> userRoleRepository.findByName(roleName.toUpperCase())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roleEntities);
        return user;
    }

}
