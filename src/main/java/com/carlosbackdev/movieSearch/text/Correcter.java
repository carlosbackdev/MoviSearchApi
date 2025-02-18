
package com.carlosbackdev.movieSearch.text;

//import java.util.*;
//import org.languagetool.JLanguageTool;
//import org.languagetool.language.Spanish;
//import org.languagetool.rules.RuleMatch;
//import java.io.IOException;


public class Correcter {
//     String frase; 
//    
//    public String corregir (String frase) {
//          if (frase == null) {
//            throw new IllegalArgumentException("La frase no puede ser nula");
//        }
//        JLanguageTool languageTool = new JLanguageTool(new Spanish());      
//        
//        StringBuilder fraseCorregida = new StringBuilder(frase);
//            int offset = 0;
//        try{
//            List<RuleMatch> matches = languageTool.check(frase);  
//
//            for (RuleMatch match : matches) {
//                if (!match.getSuggestedReplacements().isEmpty()) {
//                    String suggestion = match.getSuggestedReplacements().get(0); 
//                    fraseCorregida.replace(
//                        match.getFromPos() + offset,
//                        match.getToPos() + offset,
//                        suggestion
//                    );
//                    offset += suggestion.length() - (match.getToPos() - match.getFromPos());
//                }
//            }
//            
//        } catch(IOException e){
//              e.printStackTrace();            
//        }
//        return fraseCorregida.toString();
//    }
}
