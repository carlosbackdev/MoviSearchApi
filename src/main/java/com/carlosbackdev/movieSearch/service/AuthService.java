
package com.carlosbackdev.movieSearch.service;

import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return generateToken(user.getEmail());
        }
        return null;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private String generateToken(String email) {
        String secretKey = "mySecretKey"; // Cambia esto por una clave segura en producción
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 600000)) // 10 minutos
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }
}