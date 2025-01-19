
package com.carlosbackdev.movieSearch.text;

import java.util.*;
import java.util.regex.*;

public class NumberExtractor {
    

    public List<Integer> extractYears(String phrase) {
        List<Integer> years = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b(19[2-9][0-9]|20[0-4][0-9]|2050|[2-9]0)\\b");
        Matcher matcher = pattern.matcher(phrase);

        while (matcher.find()) {
            String match = matcher.group();
            if (match.length() == 2) {
                int startYear = 1900 + Integer.parseInt(match);
                for (int i = 0; i < 10; i++) {
                    years.add(startYear + i);
                }
            } else {
                int year = Integer.parseInt(match);
                years.add(year);
            }
        }
        
        return years;
    }      
    
}
