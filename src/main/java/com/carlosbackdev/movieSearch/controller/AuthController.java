
package com.carlosbackdev.movieSearch.controller;

import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.service.UserService;
import com.carlosbackdev.movieSearch.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        // Verificar si las credenciales son correctas (esto debería realizarse en el UserService)
        boolean isAuthenticated = userService.authenticate(user); 

        if (isAuthenticated) {
            String token = jwtUtil.generateToken(user.getUsername());  // Genera el token con el nombre de usuario
            return ResponseEntity.ok(token);  // Devuelve el token en la respuesta
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }
}