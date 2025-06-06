package com.bullzen.user.service;


import com.bullzen.user.entities.User;
import com.bullzen.user.exception.UserNotFoundException;
import com.bullzen.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User does not exists with username: " + username));
    }

    public User getByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundException("User does not exists with credentials: " + usernameOrEmail));
    }

}
