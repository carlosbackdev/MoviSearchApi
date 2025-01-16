
package com.carlosbackdev.movieSearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class TextProcessingService {
    
    @Autowired
    private TMDBService tMDBService; 
    
    @Autowired
    private TextAnalysisUtils textAnalysisUtils;

    public  Object processPhrase(String phrase) {
         if (phrase == null || phrase.trim().isEmpty()) {
        throw new IllegalArgumentException("La frase no puede estar vacía");
    }
        // Paso 1: Corregir ortografía
        String correctedPhrase = correctSpelling(phrase);

        // Paso 2: Extraer palabras clave
        List<String> keywords = extractKeywords(correctedPhrase);

        // Paso 3: Extraer nombres propios
        List<String> properNames = extractProperNames(correctedPhrase);

        // Paso 4: Extraer números
        List<Integer> numbers = extractNumbers(correctedPhrase);

        // Retornar el resultado de TMDB
        return tMDBService.fetchMovies(keywords, properNames, numbers);
    }

}
