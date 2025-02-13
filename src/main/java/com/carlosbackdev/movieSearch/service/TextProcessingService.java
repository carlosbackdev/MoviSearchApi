
package com.carlosbackdev.movieSearch.service;

import com.carlosbackdev.movieSearch.service.SynonymService;
import com.carlosbackdev.movieSearch.service.SynonymServiceTv;
import com.carlosbackdev.movieSearch.service.TMDBService;
import com.carlosbackdev.movieSearch.utils.TextAnalysisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TextProcessingService {
    
    @Autowired
    private TMDBService tMDBService; 
    
    @Autowired
    private SynonymService synonymService;
    
    @Autowired
    private SynonymServiceTv synonymServiceTv;
    
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
        System.out.println( "frase "+correctedPhrase);
        
          // Paso 2: traducir frase corregida
//         String translatedPhrase = null;
//        try {
//            translatedPhrase = googleTranslateService.translate(correctedPhrase, "en"); // Traducimos al inglés
//            System.out.println("Frase traducida: " + translatedPhrase);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        // Paso 2.1: traducir frase directamente sin corregir
        String phraseEnglish = null;
        try {
            phraseEnglish = googleTranslateService.translate(phrase, "en"); // Traducimos al inglés
        } catch (Exception e) {
            e.printStackTrace();
        }
        String phraseEnglishMinus=phraseEnglish.toLowerCase();
        System.out.println("frase ingles" + phraseEnglishMinus);
        
        // Paso 3: Extraer palabras clave
        List<String> keywords = textAnalysisUtils.extractKeywords(phraseEnglishMinus);
              System.out.println("Keywords con generos" + keywords);
              
        // Paso 4: Extraer nombres propios
        List<String> properNames = textAnalysisUtils.extractProperNames(correctedPhrase);
        
        //depurar palabra clave quitando nombres de esta
        keywords.removeIf(properNames::contains);
        
        //Paso 5: Ver el media tipo (tv,movie)
        String media = textAnalysisUtils.determineMediaType(keywords);
        keywords = textAnalysisUtils.filterKeywords(keywords, media);
        System.out.println("Keywords sin media: " + keywords);
        
        
         // Paso 6: Obtener sinónimos y comparar con los géneros
         //si es tv o movie diferentes generos
         System.out.println("tipo de: " + media);
         List<String> genreIds = new ArrayList<>();
         if("movie".equalsIgnoreCase(media != null ? media.trim() : "")){
            Set<Integer> detectedGenres = synonymService.compareWithGenres(keywords);
                for (Integer genreId : detectedGenres) {
                    genreIds.add(String.valueOf(genreId));
                    System.out.println("Géneros detectados: " + detectedGenres);
                }             
         }else{
             Set<Integer> detectedGenres = synonymServiceTv.compareWithGenresTv(keywords);
             for (Integer genreId : detectedGenres) {
                    genreIds.add(String.valueOf(genreId));
                    System.out.println("Géneros detectados: " + detectedGenres);
                }    
         }
     
             
        // Paso 5: Extraer números
        List<Integer> years = textAnalysisUtils.extractNumbers(phrase);
        
        //paso 6: Extraer Paises
        List<String> country = textAnalysisUtils.extractCountry(phraseEnglish,properNames);
        
        //PASO 7 DEBO OBTENER LOS NUMROS ID DE LOS NOMBRES 
        if(!properNames.isEmpty()){
            Object personIdResult = tMDBService.personId(properNames);
        }

        //PASO 9 OBTENER LOS NUMERO ID DE LA PALABRAS CLAVE
        //Depurar Keyword quitando los generos
        synonymService.removeMatchedKeywords(keywords);
        System.out.println("Keywords sin generos: " + keywords);
        
        if(!keywords.isEmpty()){
            Object keywordsIdResult = tMDBService.keywordsId(keywords);
        }
        
        
        System.out.println(phrase);
        System.out.println("Frase traducida sin corregir: " + phraseEnglish);
        System.out.println("Palabras clave extraídas: " + keywords);
        System.out.println("nombres propios: "+properNames);
        System.out.println("años detectados: " + years);
        System.out.println("pais detectado: " + country);
        System.out.println("personas IDS: " + properNames);
        System.out.println("Keywords Ids: " + keywords);
        
        
        Object moviesData = tMDBService.fetchMovies(phraseEnglish, genreIds, properNames, years, keywords, country, media);   
        ObjectMapper mapper = new ObjectMapper(); 
        try {
            String prettyResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(moviesData);
            System.out.println("Resultado de fetchMovies (formato legible): " + prettyResult);
       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviesData;
    }

}
