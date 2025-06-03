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

    public Optional<User> getByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

}
