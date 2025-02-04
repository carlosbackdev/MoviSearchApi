
package com.carlosbackdev.movieSearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class SynonymService {

    public static final Map<Integer, List<String>> genreKeywords = new HashMap<>();
    static {
        genreKeywords.put(28, Arrays.asList("action", "fire", "shot", "gun", "power"
            ,"explosion", "adrenaline", "chase", "mission", "fight", "struggle"
            ,"weapon", "danger", "intense", "activity"));
        genreKeywords.put(12, Arrays.asList("adventure", "escapade", "venture"
            , "journey", "expedition", "quest", "exploration", "treasure", "daring"
            , "explorer","anything"));
        genreKeywords.put(16, Arrays.asList("animation", "anime", "cartoon","animated"
            , "family-friendly", "fantasy", "imaginative","child","kid","children"
            ,"colorful", "fantastic", "adventure", "childlike", "imaginative"));
        genreKeywords.put(35, Arrays.asList("comedy", "fun","join", "funny","comical","humor","humorous","laugh","diverting","absurd"
            , "hilarity", "satire", "parody", "joke","happy","happier", "laughable", "amusing", "entertainment"
            ,"sarcasm", "slapstick", "hilarity", "quirky", "gags", "clumsy", "prank","meme"));
        genreKeywords.put(80, Arrays.asList("crime", "dead", "death","resolved","puzzle","investigate"
            , "heist", "robbery", "justice", "criminal", "underworld", "law"
            ,"mystery", "undercover", "mobster", "theft", "clue", "interrogation", "alibi"));
        genreKeywords.put(99, Arrays.asList("documentary", "documental","real"
            , "truth", "education", "informative", "real-life", "biography"
            , "analysis", "perspective", "insight", "study", "archive", "expose", "reporting"));
        genreKeywords.put(18, Arrays.asList("drama", "dramatic", "intense","dram","excitement","furor","morbid"
            , "conflict", "emotional", "realism", "tragedy", "character-driven"
            ,"confession", "betrayal", "struggle", "adversity", "sacrifice", "realistic", "melancholy"
            ,"cry","crying","life"));
        genreKeywords.put(10751, Arrays.asList("family","familiar", "company", "friend","public","kid","child","son","parent","people"
            , "together", "bond", "happy", "support", "home", "children", "values", "fun","anything"
            ,"togetherness", "fun-filled", "bonding", "parenthood", "siblings", "generation","nice"));
        genreKeywords.put(14, Arrays.asList("fantasy", "imagination", "dream","distopic","unreality","fiction","illusion"
            , "myth", "legend", "otherworldly","portal", "realm","anything"));
        genreKeywords.put(36, Arrays.asList("history", "real", "true","events","chronicle", "heritage"
            , "legacy", "historical", "past", "era", "tradition"
            ,"ancient", "timeline", "epoch", "dynasty", "archives", "memories"));
        genreKeywords.put(27, Arrays.asList("horror","fear", "aversion", "panic","revulsion","repulsion","abomination","fear","cruelity"
            ,"scary", "haunting", "darkness", "killer", "creepy", "nightmare", "chilling"
            ,"haunted", "curse", "scream", "terror", "paranormal", "sinister", "blood"));
        genreKeywords.put(10402, Arrays.asList("music", "song", "musical","sing"
            , "concert", "orchestra", "dance", "artist", "rhythm", "melody", "soundtrack", "band"
            ,"lyric", "choir", "harmonica", "acoustic", "festival", "performance", "melody", "vocalist"));
        genreKeywords.put(9648, Arrays.asList("mystery", "discover", "trama","ocult","ghost","problem","puzzle","secret","enigma"
            , "clue", "unknown", "hidden", "fog", "curiosity", "plot", "suspense", "twist"
            ,"investigation", "trail", "whodunit", "puzzle", "hint", "conspiracy"));
        genreKeywords.put(10749, Arrays.asList("romance", "romantic", "love", "darling","girlfriend","boyfriend","lovely","valentin","passion"
            , "relationship", "kiss", "wedding", "heart", "crush", "soulmate", "desire"
            , "flirt", "romantic", "happiness", "bond", "affection", "longing", "spark", "heartfelt"
            ,"cry","crying","relationships","couple"));
        genreKeywords.put(878, Arrays.asList("science", "fiction", "sci","fi","sci-fy","scify","distopic","futurism"
            ,"inteligencia", "high-tech", "innovation", "dystopia", "technology"
            , "quantum", "parallel", "alien-life", "future-tech", "cosmic", "time-travel"));
        genreKeywords.put(10770, Arrays.asList("tv", "sofa", "couch","alone","bored","bad","normal","time","variate"
            ,"pilot", "streaming", "network", "viewer", "cliffhanger"));
        genreKeywords.put(53, Arrays.asList("thriller", "tension", "shocker","enigma","question","difficulty"
            , "suspenseful", "intense", "edge", "fear", "chase", "nail-biting", "spy", "investigation"
            ,"thrill", "riddle", "intensity", "conspiracy", "risk", "plot-twist", "tension", "reveal"));
        genreKeywords.put(10752, Arrays.asList("war", "belic", "world","fighting","warfare"
            , "army", "strategy", "conflict", "trenches", "bravery"
            , "patriotism", "wartime", "weaponry", "invasion"));
        genreKeywords.put(37, Arrays.asList("western", "oest", "west","cowboy"
            , "sheriff", "saloon", "prairie", "gunslinger"
            ,"frontier", "outlaws", "cattle", "wild-west","jeans"));
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