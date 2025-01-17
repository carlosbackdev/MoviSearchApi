
package com.carlosbackdev.movieSearch.text;

import java.util.*;
import java.util.stream.Collectors;

public class KeywordExtractor {
  
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "the", "is", "in", "at", "of", "on", "and", "a", "to", "it",
            "for", "with", "as", "by", "an", "be", "this", "that", 
            "which", "or", "not", "but", "are", "was", "were", "so", 
            "if", "then", "there", "their", "from", "have", "has", "had",
            "do", "does", "did", "will", "would", "can", "could", "should",
            "may", "might", "must", "say", "says", "said", "get", "got",
            "make", "makes", "made", "go", "goes", "went", "see", "sees",
            "seen", "come", "comes", "came", "think", "thinks", "thought",
            "know", "knows", "knew", "take", "takes", "took", "use", "uses",
            "used", "look", "looks", "looked", "like", "likes", "liked"
    ));

    public List<String> extractKeywords(String phrase) {
        if (phrase == null || phrase.isEmpty()) {
            return new ArrayList<>();        }

        return new ArrayList<>(
            Arrays.stream(phrase.toLowerCase().split("\\W+"))
                .filter(word -> word.length() > 2) 
                .filter(word -> !STOP_WORDS.contains(word)) 
                .distinct() 
                .collect(Collectors.toList()) 
        );
    }
}
