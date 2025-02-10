
package com.carlosbackdev.movieSearch.controller;


import com.carlosbackdev.movieSearch.model.Comment;
import com.carlosbackdev.movieSearch.model.LikeComment;
import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.repository.CommentRepository;
import com.carlosbackdev.movieSearch.repository.LikeCommentRepository;
import com.carlosbackdev.movieSearch.repository.UserRepository;
import com.carlosbackdev.movieSearch.service.CommentService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private CommentRepository commentRepository;
    
    
    @Autowired
    private UserRepository userRepository;
    
        
    @Autowired
    private LikeCommentRepository likeCommentRepository;
    
    @GetMapping("/get/{movieId}")
    public List<Comment> getCommentsByMovieId(@PathVariable Long movieId) {
        return commentService.getCommentsByMovieId(movieId);  // Devuelve los comentarios para la película
    }
    
    
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            // Elimina el prefijo "Bearer " del token
            String jwtToken = token.replace("Bearer ", "");
            // Desencripta el token

            Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

            // Extrae el userId
            Long userId = Long.parseLong(claims.get("userId").toString());

        // Continúa con la lógica de negocio
        Long movieId = Long.parseLong(request.get("movieId"));
        String commentText = request.get("comment");
        
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Crea el objeto Comment
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMovieId(movieId);
        comment.setComment(commentText);
        comment.setLikesCount(0); // Inicializa los likes en 0

        // Guarda el comentario en la base de datos
        commentRepository.save(comment);

            return ResponseEntity.ok(Collections.singletonMap("message", "comentario subido con exito."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al subir el comentario.");
        }
    }
    
    @DeleteMapping("/delete/comment")
    public ResponseEntity<?> deleteComment(
            @RequestBody Map<String, Object> request,
            @RequestHeader("Authorization") String token) {
        try {

            // Extrae el userId desde el token
            String jwtToken = token.replace("Bearer ", "");
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

            Long userId = Long.parseLong(claims.get("userId").toString());

            // Extrae la listId y movieId del request
            Long id = Long.parseLong(request.get("id").toString());

            // Llama al servicio para eliminar la película de la lista
            commentRepository.deleteById(id);

             return ResponseEntity.ok(Collections.singletonMap("message", "Comentario eliminado correctamente."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la película.");
        }
    }
    
    @PostMapping("/like/{id_listComment}")
    public ResponseEntity<?> likeComment(@PathVariable Long id_listComment, 
                                         @RequestHeader("Authorization") String token) {
        try {
            // Extrae el userId desde el token
            String jwtToken = token.replace("Bearer ", "");
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();
            Long userId = Long.parseLong(claims.get("userId").toString());

            // Verifica si ya existe un like del usuario en ese comentario
            boolean alreadyLiked = likeCommentRepository.existsByUserIdAndListId(userId, id_listComment);
            if (alreadyLiked) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Ya has dado like a este comentario."));
            }

            // Si no existe, guarda el nuevo like
            LikeComment newLike = new LikeComment();
            newLike.setUserId(userId);
            newLike.setListId(id_listComment);
            likeCommentRepository.save(newLike);

            // Incrementa el contador de likes en la tabla de comentarios
            Comment comment = commentRepository.findById(id_listComment)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

            comment.setLikesCount(comment.getLikesCount() + 1);
            commentRepository.save(comment);

            return ResponseEntity.ok(Collections.singletonMap("message", "Like agregado correctamente."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al dar like al comentario.");
        }
    }

    
}
