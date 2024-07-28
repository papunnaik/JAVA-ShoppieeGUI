package com.shoppiee.controller;

import com.shoppiee.model.User;
import com.shoppiee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody User user) {
        User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword(), user.getRole());
        return ResponseEntity.ok(authenticatedUser != null);
    }

    @GetMapping("/getUserType/{username}")
    public ResponseEntity<String> getUserType(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserType(username));
    }

    @PostMapping("/createAccount")
    public ResponseEntity<User> createAccount(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/removeEmployee")
    public ResponseEntity<Void> removeEmployee(@RequestBody User user) {
        userService.removeUser(user.getUsername());
        return ResponseEntity.ok().build();
    }
}

