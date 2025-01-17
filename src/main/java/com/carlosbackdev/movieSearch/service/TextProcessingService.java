
package com.carlosbackdev.movieSearch.service;

import com.carlosbackdev.movieSearch.service.SynonymService;
import com.carlosbackdev.movieSearch.service.TMDBService;
import com.carlosbackdev.movieSearch.utils.TextAnalysisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class TextProcessingService {
    
    @Autowired
    private TMDBService tMDBService; 
    
    @Autowired
    private SynonymService synonymService;
    
    @Autowired
    private TextAnalysisUtils textAnalysisUtils;
    
    @Autowired
    private GoogleTranslateService googleTranslateService;
    
    public  Object processPhrase(String phrase) {
         if (phrase == null || phrase.trim().isEmpty()) {
        throw new IllegalArgumentException("La frase no puede estar vacía");
    }
        // Paso 1: Corregir ortografía
        String correctedPhrase = textAnalysisUtils.correctSpelling(phrase);
        
          // Paso 2: traducir frase
         String translatedPhrase = null;
        try {
            translatedPhrase = googleTranslateService.translate(correctedPhrase, "en"); // Traducimos al inglés
            System.out.println("Frase traducida: " + translatedPhrase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        // Paso 3: Extraer palabras clave
        List<String> keywords = textAnalysisUtils.extractKeywords(translatedPhrase);
        System.out.println("Palabras clave extraídas: " + keywords);
      
        // Paso 4: Extraer nombres propios
        List<String> properNames = textAnalysisUtils.extractProperNames(translatedPhrase);
        
         // Paso 4: Obtener sinónimos y comparar con los géneros
        Set<Integer> detectedGenres = synonymService.compareWithGenres(keywords);
        List<String> genreIds = new ArrayList<>();
            for (Integer genreId : detectedGenres) {
                genreIds.add(String.valueOf(genreId));
            }
             System.out.println("Géneros detectados: " + detectedGenres);
        // Paso 5: Extraer números
        List<Integer> numbers =     textAnalysisUtils.extractNumbers(translatedPhrase);

        // Retornar el resultado de TMDB
        return tMDBService.fetchMovies(genreIds, properNames, numbers);
    }

}
