
package com.carlosbackdev.movieSearch.controller;

import com.carlosbackdev.movieSearch.service.TextProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/text")
public class TextController {

    @Autowired
    private TextProcessingService textProcessingService;
    
    @PostMapping("/process")
    public ResponseEntity<?> processText(@RequestBody String phrase) {
        Object response = textProcessingService.processPhrase(phrase);
        return ResponseEntity.ok(response);
    }
}
