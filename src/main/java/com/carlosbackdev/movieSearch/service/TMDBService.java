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


    public Object fetchMovies(String phrase, List<String> genreIds, List<String> properNames, List<Integer> year,List<String> keywords,List<String> country) {
        OkHttpClient client = new OkHttpClient();
        String query = QueryBuilder.buildTMDBQuery(phrase,genreIds, properNames, year,API_KEY,keywords,country);
        String url = API_URL + query;
        System.out.println("consulta final" + url);
        System.out.println("consulta final" + keywords);

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
                
                return objectMapper.readValue(responseBody, Object.class);
            } else {

                throw new RuntimeException("Error al consultar la API de TMDB");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar la API de TMDB", e);
        }
                
  }
}