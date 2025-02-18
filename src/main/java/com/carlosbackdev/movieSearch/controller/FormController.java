
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FormController {
        
    @Autowired 
    private EmailService emailService; 

@PostMapping("/form")
public ResponseEntity<?> form(@RequestBody Map<String, String> formData) {
    try {
        String nombre = formData.get("nombre");
        String email = formData.get("email");
        String mensaje = formData.get("mensaje");

        new Thread(() -> {
            try { 
                emailService.enviarCorreo("carloslospinos@gmail.com", "fromulario", 
                    "Correo de: " + nombre + " (" + email + ")\n\nMensaje:\n" + mensaje);
            } catch (MessagingException e) { 
                e.printStackTrace(); 
            } 
        }).start(); 

        return ResponseEntity.ok(Collections.singletonMap("message", "Correo enviado con éxito"));
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body("Error al enviar el correo: " + e.getMessage());
    }
}
}
