
package com.carlosbackdev.movieSearch.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public class QueryBuilder {
    public static String buildTMDBQuery(
            List<String> genreIds, List<String> properNames, List<Integer> year,
            String API_KEY, List<String> keywords,List<String> country ) {
        
        StringBuilder queryBuilder = new StringBuilder("?api_key=" + API_KEY);
        if (!keywords.isEmpty()) {
            queryBuilder.append("&with_keywords=").append(String.join(",", keywords));
        }
        if (!properNames.isEmpty()) {
            queryBuilder.append("&with_people=").append(String.join(",", properNames));
        }
        if (!year.isEmpty()) {
            queryBuilder.append("&year=").append(year.get(0));
        }
        return queryBuilder.toString();
    }
}
