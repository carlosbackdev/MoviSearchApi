package com.carlosbackdev.movieSearch.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.carlosbackdev.movieSearch.utils.QueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@Service
public class TMDBService {
    
    @Value("${tmdb.api.key}")
    private String API_KEY;
    @Value("${tmdb.api.url}")
    private String API_URL;
    
    @Autowired
    private ChatService chatService;
    
    public Object personId(List<String> properNames) {        
        OkHttpClient client = new OkHttpClient();
        String query = "search/person?api_key=" + API_KEY + "&query=" + URLEncoder.encode(String.join(",", properNames), StandardCharsets.UTF_8);
        String url = API_URL + query;

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Accept", "application/json") 
            .addHeader("Authorization", "Bearer " + API_KEY)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
  
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode resultsNode = rootNode.path("results");
                
                List<String> personIds = new ArrayList<>();           
                for (JsonNode personNode : resultsNode) {
                    String personId = String.valueOf(personNode.path("id").asInt());
                    personIds.add(personId);
                }
                properNames.clear();
                properNames.addAll(personIds);

                return personIds;
            } else {

                throw new RuntimeException("Error al consultar la API de TMDB");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar la API de TMDB", e);
        }
    }
    public Object keywordsId(List<String> keywords) {        
        OkHttpClient client = new OkHttpClient();
        List<String> keywordsIds = new ArrayList<>(); 
        
        for(String keyword : keywords){
            String query = "search/keyword?api_key=" + API_KEY + "&query=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String url = API_URL + query;
            System.out.println("url: " + url);

            Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json") 
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(responseBody);
                    JsonNode resultsNode = rootNode.path("results");
                              
                    for (JsonNode personNode : resultsNode) {
                        String keyId = String.valueOf(personNode.path("id").asInt());
                        keywordsIds.add(keyId);
                    }
                    
                } else {

                    throw new RuntimeException("Error al consultar la API de TMDB");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error al consultar la API de TMDB", e);
            }
            
        }
        if(!keywordsIds.isEmpty()){
            keywords.clear();
            keywords.addAll(keywordsIds);            
        }
        return keywordsIds;
    }

    public Object fetchMovies(String phrase, List<String> genreIds, List<String> properNames, List<Integer> year, List<String> keywords, List<String> country, String media) {
        
        if(genreIds.size()>3 || keywords.size()>30 || phrase.length()>75){
            String queryIa=chatService.getNewQuery(phrase);
                return fetchMoviesWithIA(queryIa);
        }
        
        OkHttpClient client = new OkHttpClient();
        String query = QueryBuilder.buildTMDBQuery(phrase, genreIds, properNames, year, API_KEY, keywords, country, media);
        String url = API_URL + query + "&page=1"; 
        
        System.out.println("Consulta inicial: " + url);

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer " + API_KEY)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {            
                throw new RuntimeException("Error al consultar la API de TMDB: " + response.code());
            }

            String responseBody = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> resultMap = objectMapper.readValue(responseBody, Map.class);

            int totalResults = (int) resultMap.getOrDefault("total_results", 0);
            
            if (totalResults < 3) {
                System.out.println("No se encontraron películas, redirigiendo a otro proceso...");
                String queryIa=chatService.getNewQuery(phrase);
                return fetchMoviesWithIA(queryIa);
            }

            int totalPages = Math.min((int) resultMap.get("total_pages"), 30); // Máximo 20 páginas
            int randomPage = (int) (Math.random() * totalPages) + 1;
            String randomPageUrl = API_URL + query + "&page=" + randomPage + "&language=es-ES";
            System.out.println("Consulta con página aleatoria: " + randomPageUrl);

            Request randomPageRequest = new Request.Builder()
                .url(randomPageUrl)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

            try (Response randomPageResponse = client.newCall(randomPageRequest).execute()) {
                if (randomPageResponse.isSuccessful()) {
                    String randomPageResponseBody = randomPageResponse.body().string();
                    return objectMapper.readValue(randomPageResponseBody, Map.class);
                } else {
                    throw new RuntimeException("Error al consultar la API de TMDB con la página aleatoria");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al consultar la API de TMDB", e);
   }       
}
    private Object fetchMoviesWithIA(String queryIa) {
        OkHttpClient client = new OkHttpClient();
        String url = API_URL+queryIa; 
        System.out.println("Consulta IA: " + url);

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer " + API_KEY)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Error al consultar la API de TMDB: " + response.code());
            }

            String responseBody = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> resultMap = objectMapper.readValue(responseBody, Map.class);

            return resultMap; 
        } catch (IOException e) {
            throw new RuntimeException("Error al consultar la API de TMDB", e);
        }
    }



}