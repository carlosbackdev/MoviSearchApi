
package com.carlosbackdev.movieSearch.text;

import java.util.*;
import java.util.stream.Collectors;

public class KeywordExtractor {
  
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "the", "and", "for", "with", "this", "that", "which", "but", "are","change",
        "was", "were", "then", "there", "their", "from", "have", "has", "had","try",
        "does", "did", "will", "would", "can", "could", "should", "you","there","need",
        "may", "might", "must", "say", "says", "said", "get", "got", "they", "here",
        "make", "makes", "made", "goes", "went", "see", "sees","that","with","would",
        "seen", "come", "comes", "came", "think", "thinks", "thought","look","think",
        "know", "knows", "knew", "take", "takes", "took", "use", "uses","say","want",
        "used", "look", "looks", "looked", "like", "likes", "liked","show","put","ask",
        "about", "above", "after", "again", "against", "all", "also","found","find","add",
        "any", "because", "before", "being", "below", "between", "both","begin","began",
        "during", "each", "few", "following", "however", "into", "more","start","word",
        "most", "other", "over", "same", "since", "some", "such", "through","when","many",
        "under", "until", "upon", "where", "while", "within", "without","some","our",
        "best", "worst", "better", "worse", "great", "top", "low", "high", "favorite",
        "watch","recomended","then","recommend","recommends","down","left","rigth",
        "them","say","give","cacth","will","myself","something","anything","some","someone","lot",
        "ecommend","ecommends","feel","today","tomorrow","tell","tells","says","give",
        "not","tvs","film","films","shows","show","pass"
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
