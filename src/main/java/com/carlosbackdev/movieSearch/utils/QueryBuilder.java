
package com.carlosbackdev.movieSearch.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.stream.Collectors;
import java.util.*;

public class QueryBuilder {
    public static String buildTMDBQuery(
            String phrase, List<String> genreIds, List<String> properNames, List<Integer> year,
            String API_KEY, List<String> keywords,List<String> country,String media ) {         
        
        StringBuilder queryBuilder = new StringBuilder("discover/"+ media +"?api_key=" + API_KEY);
        
   
        
        if (!genreIds.isEmpty()) {
            queryBuilder.append("&with_genres=").append(String.join(",", genreIds));
        }
        if (!properNames.isEmpty()) {
            queryBuilder.append("&with_people=").append(String.join("|", properNames));
        }
        if (!year.isEmpty()) {
            StringBuilder yearsParam = new StringBuilder();
            for (int i = 0; i < year.size(); i++) {
               if (i > 0) {
                   yearsParam.append(",");
               }
               yearsParam.append(year.get(i));
            }
            if(media.equalsIgnoreCase("movie")){
                queryBuilder.append("&year=").append(yearsParam.toString());
            }else{
                queryBuilder.append("&first_air_date_year=").append(yearsParam.toString());
            }
        }
        if (!country.isEmpty()) {
            queryBuilder.append("&with_origin_country=").append(String.join(",", country));
        }
        if (!keywords.isEmpty()) {
            queryBuilder.append("&with_keywords=").append(String.join("|", keywords));
        }
        return queryBuilder.toString();
    }
    
}
