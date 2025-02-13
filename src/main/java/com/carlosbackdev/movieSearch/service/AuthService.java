
package com.carlosbackdev.movieSearch.service;

import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
     public User findUserByEmailOrFirebaseUid(String email, String firebaseUid) {
        // Primero, intenta buscar por el email
        User existingUser = userRepository.findByEmail(email);

        if (existingUser != null) {
            return existingUser;  // Si lo encontró, lo devuelve
        }

        // Si no lo encontró, intenta buscar por Firebase UID (si tienes ese campo en tu modelo)
        existingUser = userRepository.findByFirebaseUid(firebaseUid);

        return existingUser;  // Retorna el usuario o null si no existe
    }

    // Método para registrar un nuevo usuario con datos de Google
    public User registerNewGoogleUser(String firebaseUid, String email, String username) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setFirebaseUid(firebaseUid);  // Guarda el UID de Firebase
        return userRepository.save(newUser);   // Guarda el usuario y lo devuelve
    }

    public Map<String, Object> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = generateToken(user.getId(), user.getEmail());

            // Crear un Map para devolver el token y el username
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());

            return response;
        }
        return null; // Retorna null si las credenciales son incorrectas
    }

    public User register(User user) {
        User existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail != null) {
            throw new RuntimeException("email");
        }
        
        User existingUserByUsername = userRepository.findByUsername(user.getUsername());
        if (existingUserByUsername != null) {
            throw new RuntimeException("username");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000000)) 
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }
}