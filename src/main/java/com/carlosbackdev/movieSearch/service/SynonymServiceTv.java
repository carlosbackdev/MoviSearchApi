
package com.carlosbackdev.movieSearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class SynonymServiceTv {
    public static final Map<Integer, List<String>> genreKeywords = new HashMap<>();
    static {
        genreKeywords.put(10759, Arrays.asList("action", "adventure", "fire", "shot"
                ,"gun", "power", "explosion", "adrenaline", "speed", "chase", "rescue"
                , "struggle", "weapon", "revenge", "explosive"
                ,"danger", "intense"));
        genreKeywords.put(16, Arrays.asList("animation", "anime", "cartoon", "animated"
                , "family-friendly", "magic", "fantasy", "imaginative"
                ,"colorful", "storytelling", "fantastic", "adventure", "fairytale", "childlike", "imaginative"));
        genreKeywords.put(35, Arrays.asList("comedy", "fun", "join", "funny", "comical"
                ,"humor", "humorous", "laugh", "diverting", "absurd", "ridiculous", "hilarity"
                ,"satire", "parody", "joke", "happy", "happier", "laughable", "amusing"
                ,"entertainment", "sarcasm", "slapstick", "hilarity", "quirky"
                , "gags", "wit", "clumsy", "prank", "meme"));
        genreKeywords.put(80, Arrays.asList("crime", "dead", "death", "resolved"
                ,"puzzle", "investigate", "detective", "heist", "robbery"
                ,"justice", "criminal", "underworld", "law", "mystery", "undercover"
                ,"mobster", "clue", "interrogation", "alibi"));
        genreKeywords.put(99, Arrays.asList("documentary", "documental"
                ,"real", "truth", "education", "informative", "real-life", "biography", "analysis"
                ,"perspective", "insight", "study", "archive", "expose", "reporting"));
        genreKeywords.put(18, Arrays.asList("drama", "dramatic", "intense", "dram", "excitement"
                ,"furor", "morbid", "conflict", "relationships", "emotional", "realism", "tragedy"
                ,"character-driven", "confession", "betrayal", "struggle", "adversity", "sacrifice"
                ,"realistic", "melancholy", "cry", "crying", "life"));
        genreKeywords.put(10751, Arrays.asList("family", "familiar", "company", "friend", "friends"
                ,"public", "parent", "people", "together", "bond", "happy"
                ,"support", "home", "children", "values", "fun", "anything", "togetherness"
                , "tradition", "fun-filled", "bonding", "parenthood", "siblings", "generation", "nice"));
        genreKeywords.put(10762, Arrays.asList("kids", "child", "children", "young"
                , "educational", "learning", "creative"
                , "cartoon", "family-friendly", "innocent", "playful", "school"));
        genreKeywords.put(9648, Arrays.asList("mystery", "discover", "trama", "ocult"
                , "ghost", "problem", "puzzle", "secret", "enigma", "clue", "unknown"
                , "hidden", "fog", "curiosity", "plot", "suspense", "twist", "investigation"
                , "trail", "whodunit", "puzzle", "hint", "interrogation", "conspiracy"));
        genreKeywords.put(10763, Arrays.asList("news", "current", "events"
                , "update", "breaking", "headlines", "media", "coverage"
                , "global", "information", "broadcast", "live"));
        genreKeywords.put(10764, Arrays.asList("reality", "real", "life", "unscripted"
                , "everyday", "people", "drama", "lifestyle"
                , "show", "contest", "behind-the-scenes"
                , "truth", "real-life", "experience", "story", "journey"));
        genreKeywords.put(10765, Arrays.asList("sci-fi", "fantasy", "science", "fiction"
                , "sci", "fi", "sci-fy", "scify", "distopic", "futurism", "space"
                , "odyssey", "inteligencia", "future"
                , "high-tech", "innovation", "dystopia", "technology"
                , "quantum", "parallel", "simulation", "alien-life", "future-tech", "cosmic", "time-travel"));
        genreKeywords.put(10766, Arrays.asList("soap", "drama", "melodrama", "romance"
                , "relationships", "family", "love", "betrayal", "secrets", "passion"
                , "intrigue", "conflict", "emotional", "tragedy", "heartbreak"
                , "affair", "marriage", "divorce", "loyalty", "friendship"));
        genreKeywords.put(10767, Arrays.asList("talk", "show", "interview", "conversation"
                , "discussion", "host", "guest", "celebrity", "news", "opinion"
                , "debate", "entertainment", "current", "media"
                , "audience", "questions", "answers"));
        genreKeywords.put(10768, Arrays.asList("war", "politics"
                , "strategy", "conflict", "soldier", "battle"
                , "power", "leadership", "international"
                , "tension", "negotiation", "alliances", "strategy", "intrigue"));
        genreKeywords.put(37, Arrays.asList("western", "oest", "west", "cowboy"
                , "sheriff", "duel", "prairie"
                , "gunslinger", "ranch", "frontier", "outlaws"
                , "cattle", "wild-west", "gold", "wagon", "jeans"));

    }

    @Autowired
    private RestTemplate restTemplate;

public Set<Integer> compareWithGenres(List<String> keywords) {
    Set<Integer> detectedGenres = new HashSet<>();
    
    for (String keyword : keywords) {
        for (Map.Entry<Integer, List<String>> entry : genreKeywords.entrySet()) {
            for (String genreKeyword : entry.getValue()) {
                if (matches(keyword, genreKeyword)) {
                    detectedGenres.add(entry.getKey());
                    break;
                }
            }
        }

        Set<String> synonyms = getSynonyms(keyword);

        for (Map.Entry<Integer, List<String>> entry : genreKeywords.entrySet()) {
            for (String genreKeyword : entry.getValue()) {
                if (synonyms.stream().anyMatch(synonym -> matches(synonym, genreKeyword))) {
                    detectedGenres.add(entry.getKey());
                    break;
                }
            }
        }
    }

    return detectedGenres;
}

private boolean matches(String keyword, String genreKeyword) {
    return keyword.equalsIgnoreCase(genreKeyword) || keyword.equalsIgnoreCase(genreKeyword + "s");
}

public void removeMatchedKeywords(List<String> keywords) {
    Iterator<String> iterator = keywords.iterator();
    while (iterator.hasNext()) {
        String keyword = iterator.next();
        boolean isMatched = false;            
        for (Map.Entry<Integer, List<String>> entry : genreKeywords.entrySet()) {
            for (String genreKeyword : entry.getValue()) {
                if (matches(keyword, genreKeyword)) {
                    isMatched = true;
                    break;
                }
            }

            if (isMatched) {
                iterator.remove();
                break;
            }

            Set<String> synonyms = getSynonyms(keyword);
            for (String genreKeyword : entry.getValue()) {
                if (synonyms.stream().anyMatch(synonym -> matches(synonym, genreKeyword))) {
                    isMatched = true;
                    break;
                }
            }

            if (isMatched) {
                iterator.remove();
                break;
            }
        }
    }
}

    
        private Set<String> getSynonyms(String word) {
        Set<String> synonyms = new HashSet<>();
        String url = "https://api.datamuse.com/words?rel_syn=" + word;

        String response = restTemplate.getForObject(url, String.class);

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
    return synonyms;
    }
    
}
