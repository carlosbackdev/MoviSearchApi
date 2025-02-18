package com.carlosbackdev.movieSearch.controller;

import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.service.AuthService;
import com.carlosbackdev.movieSearch.service.EmailService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import java.io.ByteArrayInputStream;
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
    private EmailService emailService; 

    @Autowired
    private AuthService authService;
    
    @Value("${url.front}")
    private String urlAuth;

    @PostConstruct
    public void init() {
        try {
            String firebaseCredentialsJson = System.getenv("FIREBASE_CREDENTIALS");


            if (firebaseCredentialsJson != null) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseCredentialsJson.getBytes())))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            } else {
                throw new RuntimeException("❌ ERROR: No se encontró la variable de entorno FIREBASE_CREDENTIALS");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ ERROR al inicializar Firebase", e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = authService.register(user);
            System.out.println(user.getEmail()+"email de confirmar");
            
            new Thread(() -> {
            try { 
                emailService.enviarCorreo(user.getEmail(), "Confirma Correo de MoviSearch", "Estimado/a "+user.getUsername()+",</p>" 
                        + "<p>Nos complace informarle que su cuenta ha sido registrada con éxito en nuestro sistema.</p>" 
                        + "<p>Solicitamos que utilice el enlcae para confirmar su correo: " + urlAuth+"/" +user.getId()+ "</p>"
                        + "<p>Dispones de 7 dias para confirmar el correo y auntentioficar la cuenta si no sera eliminada depues de ese tiempo.</p>" 
                        + "<p>Agradecemos su confianza en nuestros servicios.</p><p>Atentamente,[MoviSearch]</p>"); 
            } catch (MessagingException e) { 
                e.printStackTrace(); 
            } 
        }).start(); 
            
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Map<String, Object> loginResponse = authService.login(user.getEmail(), user.getPassword());
        if (loginResponse != null) {
            return ResponseEntity.ok(loginResponse); 
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }
    
    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        String loginResponse = authService.confirm(id);
        System.out.println(loginResponse);
        if (loginResponse != null) {
            return ResponseEntity.ok(loginResponse); 
        } else {
            return ResponseEntity.badRequest().body("sin confirmar");
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
                  new Thread(() -> {
                    try { 
                        emailService.enviarCorreo(email, "Registro Completado", "Estimado/a "+decodedToken.getName()+",</p>" 
                                + "<p>Nos complace informarle que su cuenta ha sido registrada con éxito en nuestro sistema.</p>" 
                                + "<p>Agradecemos su confianza en nuestros servicios.</p><p>Atentamente,[MoviSearch]</p>"); 
                    } catch (MessagingException e) { 
                        e.printStackTrace(); 
                    } 
                }).start(); 
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
