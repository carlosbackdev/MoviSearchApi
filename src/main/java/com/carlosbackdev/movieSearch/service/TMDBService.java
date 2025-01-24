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
import org.springframework.http.ResponseEntity;

@Service
public class TMDBService {
    
    @Value("${tmdb.api.key}")
    private String API_KEY;
    @Value("${tmdb.api.url}")
    private String API_URL;
    
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

public Object fetchMovies(String phrase, List<String> genreIds, List<String> properNames, List<Integer> year, List<String> keywords, List<String> country) {
    OkHttpClient client = new OkHttpClient();
    String query = QueryBuilder.buildTMDBQuery(phrase, genreIds, properNames, year, API_KEY, keywords, country);
    int totalPages = 0;
    int pageNumber = 1;
    String url = API_URL + query + "&page=" + pageNumber;
    System.out.println("Consulta final: " + url);
    System.out.println("Consulta final con palabras clave: " + keywords);

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

            Map<String, Object> resultMap = objectMapper.readValue(responseBody, Map.class);
            totalPages = (int) resultMap.get("total_pages");

            // Ahora, hacemos una consulta con una página aleatoria
            if(totalPages>500){
                totalPages=500;
            }
            int randomPage = (int) (Math.random() * totalPages) + 1; 
            String randomPageUrl = API_URL + query + "&page=" + randomPage;
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
                    Map<String, Object> randomPageResultMap = objectMapper.readValue(randomPageResponseBody, Map.class);

                    List<Map<String, Object>> randomPageResults = (List<Map<String, Object>>) randomPageResultMap.get("results");

                    for (Map<String, Object> item : randomPageResults) {
                        Integer movieId = (Integer) item.get("id");
                        String mediaType = (String) item.get("media_type");

                        String randomPageTranslateUrl = API_URL + mediaType + "/" + movieId + "/translations?api_key=" + API_KEY;
                        Request randomPageTranslateRequest = new Request.Builder()
                            .url(randomPageTranslateUrl)
                            .get()
                            .addHeader("Accept", "application/json")
                            .build();

                        try (Response randomPageTranslateResponse = client.newCall(randomPageTranslateRequest).execute()) {
                            if (randomPageTranslateResponse.isSuccessful()) {
                                String randomPageTranslateResponseBody = randomPageTranslateResponse.body().string();
                                Map<String, Object> translateData = objectMapper.readValue(randomPageTranslateResponseBody, Map.class);
                                List<Map<String, Object>> translations = (List<Map<String, Object>>) translateData.get("translations");

                                String translatedTitle = null;
                                for (Map<String, Object> translation : translations) {
                                    String iso = (String) translation.get("iso_639_1");
                                    String countr = (String) translation.get("iso_3166_1");

                                    if ("es".equals(iso) && translatedTitle == null) {
                                        Map<String, String> data = (Map<String, String>) translation.get("data");
                                        translatedTitle = data.get("title");
                                    }

                                    if ("es".equals(iso) && "MX".equals(country) && translatedTitle == null) {
                                        Map<String, String> data = (Map<String, String>) translation.get("data");
                                        translatedTitle = data.get("title");
                                    }
                                }
                                
                                if (translatedTitle != null) {
                                    item.put("title", translatedTitle);
                                }
                            }
                        }
                    }

             
                    return randomPageResultMap;
                    
                    
                } else {
                    throw new RuntimeException("Error al consultar la API de TMDB con la página aleatoria");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error al consultar la API de TMDB con la página aleatoria", e);
            }
        } else {
            throw new RuntimeException("Error al consultar la API de TMDB");
        }
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Error al consultar la API de TMDB", e);
    }
}


}