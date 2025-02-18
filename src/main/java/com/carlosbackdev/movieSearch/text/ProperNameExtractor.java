package com.carlosbackdev.movieSearch.text;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProperNameExtractor {

    public List<String> extractProperNames(String text) {
        List<String> properNames = new ArrayList<>();
        String[] words = text.split("\\s+");  // Dividir el texto en palabras

        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            // Si no es la primera palabra y la palabra está capitalizada, se considera nombre propio
            if (i > 0 && isCapitalized(word)) {
                properNames.add(word.toLowerCase());
            }
        }

        return properNames;
    }

    private boolean isCapitalized(String word) {
        return word.length() > 2 && Character.isUpperCase(word.charAt(0));  // Verifica si la palabra está capitalizada
    }
}
