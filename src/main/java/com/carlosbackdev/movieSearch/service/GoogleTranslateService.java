package com.carlosbackdev.movieSearch.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils; 

@Service
public class GoogleTranslateService {

    private static final String API_KEY = "AIzaSyCcKma4Lh8I1TchgVbRAfVMM-IFxdtHhrk"; // Reemplaza con tu clave de API

    public String translate(String text, String targetLanguage) throws Exception {
        String encodedText = URLEncoder.encode(text, "UTF-8");
        String urlStr = "https://translation.googleapis.com/language/translate/v2?q=" + encodedText + "&target=" + targetLanguage + "&key=" + API_KEY;

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        String translatedText = jsonResponse.getJSONObject("data")
                                            .getJSONArray("translations")
                                            .getJSONObject(0)
                                            .getString("translatedText");


        translatedText = StringEscapeUtils.unescapeHtml4(translatedText);
        String input = translatedText;   
        int startIndex = input.indexOf(":") + 3; 
        int endIndex = input.lastIndexOf("\"");
        String extractedText = input.substring(startIndex, endIndex);
        extractedText =extractedText.trim();
        String cleanText = extractedText.replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ\\s]", "");

        return cleanText;
    }
}
