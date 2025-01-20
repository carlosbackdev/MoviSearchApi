
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
        genreKeywords.put(28, Arrays.asList("action", "fire", "shot", "gun", "power", "combat","war"
            , "explosion", "adrenaline", "speed", "chase", "rescue", "mission", "fight", "struggle"
            ,"weapon", "revenge", "explosive", "danger", "intense", "mercenary", "fighter"));
        genreKeywords.put(12, Arrays.asList("adventure", "escapade", "venture"
            , "journey", "expedition", "quest", "exploration", "trek", "treasure", "daring"
            ,"treasure-hunting", "island", "survival", "explorer", "risk", "heroic", "map","anything"));
        genreKeywords.put(16, Arrays.asList("animation", "anime", "cartoon","animated"
            , "pixar", "disney", "family-friendly", "magic", "fantasy", "imaginative"
            ,"colorful", "storytelling", "fantastic", "adventure", "fairytale", "childlike", "imaginative"));
        genreKeywords.put(35, Arrays.asList("comedy", "fun","join", "funny","comical","humor","humorous","laugh","diverting","absurd","ridicolous"
            , "hilarity", "satire", "parody", "joke","happy","happier", "laughable", "amusing", "entertainment"
            ,"sarcasm", "slapstick", "hilarity", "quirky", "gags", "wit", "clumsy", "prank","meme"));
        genreKeywords.put(80, Arrays.asList("crime", "dead", "death","resolved","puzzle","police","investigate"
            , "detective", "heist", "robbery", "justice", "mafia", "criminal", "underworld", "law"
            ,"mystery", "undercover", "mobster", "theft", "gangster", "clue", "interrogation", "alibi"));
        genreKeywords.put(99, Arrays.asList("documentary", "documental", "animal","animals","real"
            , "truth", "education", "informative", "real-life", "biography"
            , "analysis", "perspective", "insight", "study", "archive", "expose", "reporting"));
        genreKeywords.put(18, Arrays.asList("drama", "dramatic", "intense","dram","excitement","furor","morbid"
            , "conflict", "relationships", "emotional", "realism", "tragedy", "character-driven"
            ,"confession", "betrayal", "struggle", "adversity", "sacrifice", "realistic", "melancholy"
            ,"cry","crying","life"));
        genreKeywords.put(10751, Arrays.asList("family","familiar", "company", "friend","friends","public","kid","child","son","parent","people"
            , "together", "bond", "happy", "support", "home", "children", "values", "fun","anything"
            ,"togetherness", "tradition", "fun-filled", "bonding", "parenthood", "siblings", "generation","nice"));
        genreKeywords.put(14, Arrays.asList("fantasy", "imagination", "dream","distopic","unreality","fiction","illusion"
            , "myth", "legend", "creature", "magic", "wizard", "sorcery", "unicorn", "otherworldly"
            ,"portal", "realm", "dragons", "witch", "kingdom", "epic", "hero", "magic-spell","anything"));
        genreKeywords.put(36, Arrays.asList("history", "real", "true","events","chronicle", "heritage"
            , "legacy", "historical", "past", "era", "tradition"
            ,"ancient", "timeline", "epoch", "monument", "dynasty", "revolution", "archives", "memories"));
        genreKeywords.put(27, Arrays.asList("horror","fear","demon","monster", "aversion", "panic","revulsion","repulsion","abomination","fear","cruelity"
            , "scary", "haunting", "darkness", "killer", "creepy", "evil", "nightmare", "chilling"
            ,"haunted", "curse", "scream", "terror", "paranormal", "sinister", "demonic", "blood"));
        genreKeywords.put(10402, Arrays.asList("music", "song", "musical","sing"
            , "concert", "orchestra", "dance", "artist", "rhythm", "melody", "soundtrack", "band"
            ,"lyric", "choir", "harmonica", "acoustic", "festival", "performance", "melody", "vocalist"));
        genreKeywords.put(9648, Arrays.asList("mystery", "discover", "trama","ocult","ghost","problem","puzzle","secret","enigma"
            , "clue", "unknown", "hidden", "fog", "curiosity", "plot", "suspense", "twist"
            ,"investigation", "trail", "whodunit", "dark", "puzzle", "hint", "interrogation", "conspiracy"));
        genreKeywords.put(10749, Arrays.asList("romance", "romantic", "love", "darling","girlfriend","boyfriend","lovely","valentin","passion"
            , "relationship", "kiss", "wedding", "heart", "crush", "soulmate", "desire"
            , "flirt", "romantic", "happiness", "bond", "affection", "longing", "spark", "heartfelt"
            ,"cry","crying","relationships","couple"));
        genreKeywords.put(878, Arrays.asList("science", "fiction", "sci","fi","sci-fy","scify","distopic","futurism","space","odyssey","epic"
            , "robot","inteligencia", "alien", "future", "cyber", "high-tech", "innovation", "dystopia", "technology"
            ,"spaceship", "quantum", "parallel", "simulation", "alien-life", "future-tech", "cosmic", "time-travel"));
        genreKeywords.put(10770, Arrays.asList("tv", "sofa", "couch","alone","bored","bad","normal","time","variate"
            ,"pilot", "streaming", "network", "viewer", "cliffhanger"));
        genreKeywords.put(53, Arrays.asList("thriller", "tension", "shocker","enigma","question","difficulty"
            , "suspenseful", "intense", "edge", "fear", "chase", "nail-biting", "spy", "investigation"
            ,"thrill", "riddle", "intensity", "conspiracy", "risk", "plot-twist", "tension", "reveal"));
        genreKeywords.put(10752, Arrays.asList("war", "belic", "world","nazi","hitler","military","fighting","battle","warfare"
            , "army", "soldier", "strategy", "invasion", "conflict", "trenches", "heroism", "bravery"
            ,"combat", "peace", "alliances", "patriotism", "wartime", "enemy", "weaponry", "invasion"));
        genreKeywords.put(37, Arrays.asList("western", "oest", "west","cowboy","indians"
            , "sheriff", "duel", "saloon", "prairie", "horse", "desert", "gunslinger", "ranch"
            ,"frontier", "outlaws", "guns", "shootout", "cattle", "wild-west", "gold", "wagon","jeans"));
    }

    @Autowired
    private RestTemplate restTemplate;

    public Set<Integer> compareWithGenres(List<String> keywords) {
        Set<Integer> detectedGenres = new HashSet<>();
        
        for (String keyword : keywords) {
            for (Map.Entry<Integer, List<String>> entry : genreKeywords.entrySet()) {
                for (String genreKeyword : entry.getValue()) {
                    if (genreKeyword.equalsIgnoreCase(keyword)) {
                        detectedGenres.add(entry.getKey());
                        break;
                    }
                }
            }

            Set<String> synonyms = getSynonyms(keyword);

            for (Map.Entry<Integer, List<String>> entry : genreKeywords.entrySet()) {
                for (String genreKeyword : entry.getValue()) {
                    if (synonyms.contains(genreKeyword)) {
                        detectedGenres.add(entry.getKey());
                        break;
                    }
                }
            }
        }

        return detectedGenres;
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