package com.carlosbackdev.movieSearch.utils;  
 
import java.util.ArrayList;
import org.springframework.stereotype.Component;
import java.util.List;
import com.carlosbackdev.movieSearch.text.Correcter;


@Component
public class TextAnalysisUtils {

    public String correctSpelling(String phrase) {
        if (phrase == null) {
            throw new IllegalArgumentException("La frase no puede ser nula");
        }
        Correcter correcter=new Correcter();
        String fraseCorregida=correcter.corregir(phrase);
        System.out.println( fraseCorregida);
        return fraseCorregida;
    }

    public List<String> extractKeywords(String phrase) {
            // Implementar lógica para extraer palabras clave
            List<String> keywords = new ArrayList<>();

            // Asumimos que cada palabra es una palabra clave
            String[] words = phrase.split(" ");

            for (String word : words) {
                // Agregar solo palabras con longitud mínima (por ejemplo, más de 2 caracteres)
                if (word.length() > 2) {
                    keywords.add(word.toLowerCase()); // Convertir todo a minúsculas para evitar duplicados
                }
            }

            return keywords;
        }
    

    public List<String> extractProperNames(String phrase) {
        // Implementar lógica para extraer nombres propios
        return new ArrayList<>();
    }

    public List<Integer> extractNumbers(String phrase) {
        // Implementar lógica para extraer números
        return new ArrayList<>();
    }
}