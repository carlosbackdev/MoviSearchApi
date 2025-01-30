
package com.carlosbackdev.movieSearch.text;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProperNameExtractor {

    private final StanfordCoreNLP pipeline;
    
    public ProperNameExtractor() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner"); 
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<String> extractProperNames(String text) {
        List<String> properNames = new ArrayList<>();
        String extractedText = text.replaceAll(".*\"(.*?)\".*", "$1");
        System.out.println("prueba de texto "+ extractedText);
        Annotation document = new Annotation(extractedText);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if ("PERSON".equals(ner)) {
                                    properNames.add(word.toLowerCase());
                                } else {
                                    if (isCapitalized(word.toLowerCase())) {
                                        properNames.add(word.toLowerCase());
                                    }
                                }
                            }
                        }
                        return properNames;
                    }

    private boolean isCapitalized(String word) {
        return word.length() > 2 && Character.isUpperCase(word.charAt(0));
        }
}