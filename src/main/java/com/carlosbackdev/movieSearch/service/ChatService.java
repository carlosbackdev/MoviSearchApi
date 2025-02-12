
package com.carlosbackdev.movieSearch.service;


import com.carlosbackdev.movieSearch.model.ChatHistory;
import com.carlosbackdev.movieSearch.repository.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ChatService {

    @Value("${openai.api.key}")
    private String openAiApiKey;
    
    @Value("${prompt}")
    private String prompt;
    
    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

@SuppressWarnings("unchecked")
public String getChatbotResponse(Long userId, String userPhrase) {
    String apiUrl = "https://api.openai.com/v1/chat/completions";
    System.out.println(prompt);
    // Cuerpo de la petición
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "gpt-3.5-turbo");
    requestBody.put("max_tokens", 300);
    requestBody.put("messages", List.of(
            Map.of("role", "system", "content", "Eres un asistente en una aplicación de recomendación de películas. El usuario te pide recomendaciones basadas en su estado de ánimo, preferencias, frases simples, géneros, o mas filtros, estos se obtienen y se envían las peliculas o series directamente.Tu debes  generar una respuesta amigable y emocional en formato Markdown, a la frase sin recomendar explícitamente ninguna pelicula o serie, y preguntando si quiere más recomendaciones. añade emoticonos"),
            Map.of("role", "user", "content", userPhrase)
    ));

    // Configurar la cabecera con la API Key
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(openAiApiKey);

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
    RestTemplate restTemplate = new RestTemplate();

    try {
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

        // Extraer la respuesta correctamente
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String chatResponse = (String) message.get("content"); // Ahora sí es un String

        // Guardar en la base de datos
        ChatHistory chatHistory = new ChatHistory(userId, userPhrase, chatResponse);
        chatHistoryRepository.save(chatHistory);
        Long chatId = chatHistory.getId();
        String formattedResponse = chatId + ": " + chatResponse;

        return formattedResponse;
    } catch (Exception e) {
        e.printStackTrace();
        return "Lo siento, no pude generar una respuesta en este momento.";
    }
  }
    public ChatHistory updateChatHistory(Long chatId, List<Long> movieIds) {
           ChatHistory chatHistory = chatHistoryRepository.findById(chatId)
                   .orElseThrow(() -> new RuntimeException("Chat history not found"));
           chatHistory.setPeliculasId(movieIds);
           return chatHistoryRepository.save(chatHistory);
       }
}
