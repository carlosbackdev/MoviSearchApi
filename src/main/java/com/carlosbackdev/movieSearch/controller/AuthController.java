package com.carlosbackdev.movieSearch.controller;

import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.service.AuthService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${firebase.credentials.path}")
    private String firebaseCredentialsPath;

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream(firebaseCredentialsPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Maneja el error, si el archivo no se encuentra o hay problemas con la inicialización.
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = authService.register(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Map<String, Object> loginResponse = authService.login(user.getEmail(), user.getPassword());
        if (loginResponse != null) {
            return ResponseEntity.ok(loginResponse); // Devuelve el Map con el token y el username
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> tokenMap) {
        String token = tokenMap.get("idToken");  // El token de Google viene en el body

        try {
            // Verifica el token de Google usando Firebase Admin SDK
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String firebaseUid = decodedToken.getUid();  // ID del usuario en Firebase
            String email = decodedToken.getEmail();     // Email del usuario

            // Comprueba si el usuario existe en la base de datos
            User existingUser = authService.findUserByEmailOrFirebaseUid(email, firebaseUid);

            if (existingUser == null) {
                // Si no existe, guarda el nuevo usuario
                existingUser = authService.registerNewGoogleUser(firebaseUid, email, decodedToken.getName());
            }

            // Genera un JWT para la autenticación interna
            String jwtToken = authService.generateToken(existingUser.getId(), existingUser.getEmail());

            // Devolver el JWT junto con el nombre de usuario
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("username", existingUser.getUsername());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid Google Token: " + e.getMessage());
        }
    }
}
