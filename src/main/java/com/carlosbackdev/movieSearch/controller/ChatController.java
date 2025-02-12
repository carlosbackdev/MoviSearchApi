package com.carlosbackdev.movieSearch.controller;


import com.carlosbackdev.movieSearch.model.ChatHistory;
import com.carlosbackdev.movieSearch.service.ChatService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/chatbot")
public class ChatController {

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Autowired
    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

 @GetMapping
    public ResponseEntity<?> getChatbotResponse(
            @RequestHeader(value = "Authorization", required = false) String token, 
            @RequestParam("phrase") String userPhrase) {

        Long userId = null;

        if (token != null && !token.isEmpty()) {
            try {
                String jwtToken = token.replace("Bearer ", "");
                System.out.println("JWT Token: " + jwtToken);

                Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(jwtToken)
                    .getBody();
                System.out.println("Token Claims: " + claims);
                userId = Long.parseLong(claims.get("userId").toString());
            } catch (Exception e) {
                System.out.println("Invalid or expired token: " + e.getMessage());
            }
        }
        if (userId == null) {
            userId = -1L; 
        }

        String chatbotResponse = chatService.getChatbotResponse(userId, userPhrase);
        return ResponseEntity.ok(Collections.singletonMap("response", chatbotResponse));
    }

    
      @PutMapping("/update/{chatId}")
    public ResponseEntity<?> updateChatHistory(
            @PathVariable Long chatId, 
            @RequestBody Map<String, List<Long>> requestBody) {

        // Obtener las IDs de las películas del cuerpo de la solicitud
        List<Long> movieIds = requestBody.get("movieIds");

        // Llamar al servicio para actualizar el historial de chat
        ChatHistory updatedChatHistory = chatService.updateChatHistory(chatId, movieIds);

        // Retornar una respuesta exitosa
        return ResponseEntity.ok(Collections.singletonMap("message", "Chat history updated successfully"));
    }

}
