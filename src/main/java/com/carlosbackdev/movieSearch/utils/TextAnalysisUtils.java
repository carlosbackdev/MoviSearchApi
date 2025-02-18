package com.carlosbackdev.movieSearch.utils;  
 
import java.util.ArrayList;
import org.springframework.stereotype.Component;
import java.util.List;
import com.carlosbackdev.movieSearch.text.Correcter;
import com.carlosbackdev.movieSearch.text.KeywordExtractor;
import com.carlosbackdev.movieSearch.text.ProperNameExtractor;
import com.carlosbackdev.movieSearch.text.NumberExtractor;
import com.carlosbackdev.movieSearch.text.CountryExtractor;
import java.util.stream.Collectors;


@Component
public class TextAnalysisUtils {

//    public String correctSpelling(String phrase) {
//        if (phrase == null) {
//            throw new IllegalArgumentException("La frase no puede ser nula");
//        }
//        Correcter correcter=new Correcter();
//        String fraseCorregida=correcter.corregir(phrase);
//        return fraseCorregida;
//    }

    public List<String> extractKeywords(String phrase) {
        KeywordExtractor extractor = new KeywordExtractor();
        List<String> keywords = extractor.extractKeywords(phrase);
        keywords.removeIf(word -> word.matches(".*\\d.*"));
            return keywords;
        }
    

    public List<String> extractProperNames(String phrase) {
        ProperNameExtractor extractor=new ProperNameExtractor();
        List <String> person=extractor.extractProperNames(phrase);        
        return person;
    }

    public List<Integer> extractNumbers(String phrase) {
        NumberExtractor extractor=new NumberExtractor();
        List <Integer> year=extractor.extractYears(phrase);
        return year;
    }
    
    public List<String> extractCountry(String phrase,List<String> properNames){
        CountryExtractor extractor=new CountryExtractor();
        List <String> country=extractor.extractCountry(phrase, properNames);
        return country;
    }
        
    public String determineMediaType(List<String> keywords) {
        List<String> movieKeywords = List.of("movie", "film","motion","picture","movies","films","picures",
                "motions","filme","filming","cine","cinema","cines","cinemas");        
        List<String> tvKeywords = List.of("serie", "tv", "show","series","tvs","shows","sets","set");
        String retorno="movie";
        for (String keyword : keywords) {
            if (tvKeywords.contains(keyword.toLowerCase())) {
                return "tv"; 
            }
        }
        for (String keyword : keywords) {
            if (movieKeywords.contains(keyword.toLowerCase())) {
                return "movie";
            }
        }
        return retorno;
    }
    public List<String> filterKeywords(List<String> keywords, String mediaType) {
        List<String> keywordsToRemove;

        if (mediaType.equals("movie")) {
            keywordsToRemove = List.of("movie", "film", "motion", "picture", "movies", "films", "pictures",
                    "motions", "filme", "filming", "cine", "cinema", "cines", "cinemas");
        } else {
            keywordsToRemove = List.of("serie", "tv", "show", "series", "tvs", "shows", "sets", "set");
        }
        return keywords.stream()
                .filter(keyword -> !keywordsToRemove.contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
    
}