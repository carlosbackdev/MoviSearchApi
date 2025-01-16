package com.carlosbackdev.movieSearch.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.carlosbackdev.movieSearch.utils.QueryBuilder;
import java.util.List;

@Service
public class TMDBService {

    private static final String API_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String API_KEY = "TU_CLAVE_API";

    public Object fetchMovies(List<String> keywords, List<String> properNames, List<Integer> numbers) {
        try {
        // Llamar a la API de TMDB
        RestTemplate restTemplate = new RestTemplate();
        String query = QueryBuilder.buildTMDBQuery(keywords, properNames, numbers);
        String url = TMDB_API_URL + query;
        return restTemplate.getForObject(url, Object.class);
    }catch (Exception e) {
        // Manejar el error de forma adecuada
        throw new RuntimeException("Error al consultar la API de TMDB", e);
    }
  }
}