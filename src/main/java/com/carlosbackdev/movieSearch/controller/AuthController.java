
package com.carlosbackdev.movieSearch.controller;

import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User registeredUser = authService.register(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String token = authService.login(user.getEmail(), user.getPassword());
        if (token != null) {
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }
}