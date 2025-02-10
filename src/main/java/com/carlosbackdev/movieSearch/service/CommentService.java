
package com.carlosbackdev.movieSearch.service;

import com.carlosbackdev.movieSearch.model.Comment;
import com.carlosbackdev.movieSearch.repository.CommentRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
        public List<Comment> getCommentsByMovieId(Long movieId) {
        return commentRepository.findByMovieId(movieId);  // Obtiene los comentarios de la base de datos
    }
        
    
}
