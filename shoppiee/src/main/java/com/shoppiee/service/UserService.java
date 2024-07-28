package com.shoppiee.service;

import com.shoppiee.model.User;
import com.shoppiee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password, String role) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.getRole().equals(role)) {
            return user;
        }
        return null;
    }

    public String getUserType(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getRole() : "";
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void removeUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
