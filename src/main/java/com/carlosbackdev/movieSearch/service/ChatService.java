
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
    
    @Value("${tmdb.api.key}")
    private String apiKey;
    
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
            Map.of("role", "system", "content", "Eres un asistente en una aplicación de recomendación de películas. El usuario te pide recomendaciones basadas en su estado de ánimo, preferencias, frases simples, géneros, o mas filtros, estos se obtienen y se envían las peliculas o series directamente.Tu debes  generar una respuesta amigable y emocional, a la frase sin recomendar explícitamente ninguna pelicula o serie aunque diga mejores o lo que sea, y preguntando si quiere más recomendaciones.\n" +
""),
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

    @SuppressWarnings("unchecked")
    public String getNewQuery(String userPhrase) {
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // Prompt mejorado para obtener una query exacta
        String prompt = "El usuario ha introducido la siguiente frase en un buscador de películas o series: '" 
                + userPhrase + "'.\n"
                + "Tu tarea es transformar esta frase en una URL válida de búsqueda para la API de TMDB.\n"
                + "Devuelve **únicamente** la URL sin ningún otro texto ni explicaciones, con el formato exacto:\n"
                + "'/discover/movie?api_key=API_KEY&with_genres=28&year=2020&language=es-ES'.\n"
                + "Asegúrate de incluir solo los parámetros necesarios según la intención del usuario.\n\n"
                
                + "### **Reglas para construir la query:**\n"
                + "- **IMPORTANTE:** Si la frase menciona 'serie', 'series', 'temporada' o términos relacionados, usa 'discover/tv' en lugar de 'discover/movie'.\n"
                + "- **Géneros:** Si la frase menciona géneros (acción, comedia, terror, etc.), conviértelos en el parámetro 'with_genres' usando los IDs de TMDB.\n"
                + "- **Año:** Si la frase menciona un año o rango (ej. 'películas de 1995' o 'cine de los 80'), agrégalo con 'year'.\n"
                + "- **País:** Si la frase menciona un país de origen ('películas francesas'), usa 'region'.\n"
                + "- **Palabras clave:** Si hay términos como 'viajes en el tiempo', 'robots', 'inteligencia artificial', cualquiera agrégalo en 'with_keywords'.\n"
                + "- **Películas específicas:** Si la frase menciona una película exacta ('quiero ver Interstellar'), usa 'search/movie' con 'query'.\n"
                + "- **Idioma:** Asegura que el idioma sea español con 'language=es-ES'.\n"
                + "- **Orden:** Si la frase incluye palabras como 'mejor valoradas', 'populares' o 'recientes', usa los parámetros 'sort_by' adecuados:\n"
                + "    - 'populares' → sort_by=popularity.desc\n"
                + "    - 'mejor valoradas' → sort_by=vote_average.desc&vote_count.gte=1000 (para evitar películas con pocas valoraciones)\n"
                + "    - 'recientes' → sort_by=release_date.desc\n\n"

                + "### **Ejemplos de conversión:**\n"
                + "- Entrada: \"Quiero ver una película de terror de los 90\"\n"
                + "  → Respuesta: discover/movie?api_key=API_KEY&with_genres=27&year=1990&language=es-ES\n"
                + "- Entrada: \"Películas francesas de drama\"\n"
                + "  → Respuesta: /discover/movie?api_key=API_KEY&with_genres=18&region=FR&language=es-ES\n"
                + "- Entrada: \"Quiero ver Interstellar\"\n"
                + "  → Respuesta: /search/movie?api_key=API_KEY&query=Interstellar&language=es-ES\n"
                + "- Entrada: \"Mejor película de ciencia ficción\"\n"
                + "  → Respuesta: /discover/movie?api_key=API_KEY&with_genres=878&sort_by=vote_average.desc&vote_count.gte=1000&language=es-ES\n\n"

                + "Si la frase es demasiado ambigua o no contiene información clara, responde con:\n"
                + "/discover/movie?api_key=API_KEY&language=es-ES.\n"
                + "No agregues explicaciones, solo responde con la URL generada.";

        // Cuerpo de la petición para OpenAI
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("max_tokens", 200);
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "Eres un asistente especializado en construir consultas para la API de TMDB."),
                Map.of("role", "user", "content", prompt)
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
            String chatResponse = (String) message.get("content");

            // Reemplazar "API_KEY" con la real
            String finalQuery = chatResponse.replace("API_KEY", apiKey);

            return finalQuery;

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    
    public ChatHistory updateChatHistory(Long chatId, List<Long> movieIds) {
           ChatHistory chatHistory = chatHistoryRepository.findById(chatId)
                   .orElseThrow(() -> new RuntimeException("Chat history not found"));
           chatHistory.setPeliculasId(movieIds);
           return chatHistoryRepository.save(chatHistory);
       }
}
