package com.carlosbackdev.movieSearch.utils;  
 
import java.util.ArrayList;
import org.springframework.stereotype.Component;
import java.util.List;
import com.carlosbackdev.movieSearch.text.Correcter;
import com.carlosbackdev.movieSearch.text.KeywordExtractor;
import com.carlosbackdev.movieSearch.text.ProperNameExtractor;


@Component
public class TextAnalysisUtils {

    public String correctSpelling(String phrase) {
        if (phrase == null) {
            throw new IllegalArgumentException("La frase no puede ser nula");
        }
        Correcter correcter=new Correcter();
        String fraseCorregida=correcter.corregir(phrase);
        return fraseCorregida;
    }

    public List<String> extractKeywords(String phrase) {
        KeywordExtractor extractor = new KeywordExtractor();
        List<String> keywords = extractor.extractKeywords(phrase);
            return keywords;
        }
    

    public List<String> extractProperNames(String phrase) {
        ProperNameExtractor extractor=new ProperNameExtractor();
        List <String> person=extractor.extractProperNames(phrase);        
        return person;
    }

    public List<Integer> extractNumbers(String phrase) {
        // Implementar lógica para extraer números
        return new ArrayList<>();
    }
}