package com.carlosbackdev.movieSearch.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.carlosbackdev.movieSearch.utils.QueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

@Service
public class TMDBService {
    
    @Value("${tmdb.api.key}")
    private String API_KEY;
    @Value("${tmdb.api.url}")
    private String API_URL;

    public Object fetchMovies(List<String> keywords, List<String> properNames, List<Integer> numbers) {
        try {
        // Llamar a la API de TMDB
        RestTemplate restTemplate = new RestTemplate();
        String query = QueryBuilder.buildTMDBQuery(keywords, properNames, numbers,API_KEY);
        String url = API_URL + query;
        return restTemplate.getForObject(url, Object.class);
    }catch (Exception e) {
        // Manejar el error de forma adecuada
        throw new RuntimeException("Error al consultar la API de TMDB", e);
    }
  }
}