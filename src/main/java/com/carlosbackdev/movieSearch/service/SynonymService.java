
package com.carlosbackdev.movieSearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class SynonymService {

    private static final Map<Integer, List<String>> genreKeywords = new HashMap<>();

    static {
        // Mapa de géneros y sus palabras clave principales
        genreKeywords.put(1, Arrays.asList("comedia", "reír", "divertido", "gracioso", "humor"));
        genreKeywords.put(2, Arrays.asList("acción", "aventura", "lucha"));
        genreKeywords.put(3, Arrays.asList("terror", "miedo", "suspenso","horror","repugnance"));
        // Añadir más géneros según sea necesario
    }

    @Autowired
    private RestTemplate restTemplate;

    public Set<Integer> compareWithGenres(List<String> keywords) {
        Set<Integer> detectedGenres = new HashSet<>();
        
        for (String keyword : keywords) {
            System.out.println("Procesando palabra clave: " + keyword);
            
            // Comparar las palabras clave originales con los géneros
            for (Map.Entry<Integer, List<String>> entry : genreKeywords.entrySet()) {
                for (String genreKeyword : entry.getValue()) {
                    if (genreKeyword.equalsIgnoreCase(keyword)) {
                        detectedGenres.add(entry.getKey());
                        break;
                    }
                }
            }

            // Obtener sinónimos de la palabra usando una API externa
            Set<String> synonyms = getSynonyms(keyword);
            
             System.out.println("Sinónimos de '" + keyword + "': " + synonyms); 

            // Comparar los sinónimos con los géneros
            for (Map.Entry<Integer, List<String>> entry : genreKeywords.entrySet()) {
                for (String genreKeyword : entry.getValue()) {
                    if (synonyms.contains(genreKeyword)) {
                        detectedGenres.add(entry.getKey());
                        
                         System.out.println("Género encontrado (sinónimo): " + entry.getKey());
                        break;
                    }
                }
            }
        }

        return detectedGenres;
    }

    private Set<String> getSynonyms(String word) {
        Set<String> synonyms = new HashSet<>();
        
        // Usamos la API de Datamuse para obtener los sinónimos
        String url = "https://api.datamuse.com/words?rel_syn=" + word + "&lang=es";
        
         System.out.println("Consultando sinónimos para: " + word); 
        
    String response = restTemplate.getForObject(url, String.class);
    
    System.out.println("Respuesta de la API: " + response);

    // Procesar la respuesta de la API (usando Jackson para parsear el JSON)
    if (response != null && !response.isEmpty()) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);

            for (JsonNode node : jsonNode) {
                String synonym = node.get("word").asText();
                synonyms.add(synonym);
            }

        } catch (Exception e) {
            System.err.println("Error al procesar la respuesta JSON: " + e.getMessage());
        }
    }
    
    System.out.println("Sinónimos encontrados: " + synonyms);  
    return synonyms;
    }
}