
package com.carlosbackdev.movieSearch.controller;

import com.carlosbackdev.movieSearch.dto.ListWithMoviesDTO;
import com.carlosbackdev.movieSearch.model.ListEntity;
import com.carlosbackdev.movieSearch.model.MovieList;
import com.carlosbackdev.movieSearch.repository.ListRepository;
import com.carlosbackdev.movieSearch.repository.MovieRepository;
import com.carlosbackdev.movieSearch.service.ListService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/lists")
public class ListController {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private ListService listService;
    
    @Autowired
    private ListRepository listRepository; 
    
    @Autowired
    private MovieRepository movieRepository; 

    @GetMapping("/user")
    public ResponseEntity<?> getUserLists(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("Authorization Header: " + token);
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token.replace("Bearer ", "")) 
                .getBody();

            Long userId = Long.parseLong(claims.get("userId").toString());
            List<ListEntity> userLists = listService.getUserLists(userId);
            return ResponseEntity.ok(userLists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid token"));
        }
    }
    @GetMapping("/movie")
    public ResponseEntity<?> getMovieLists(@RequestHeader("Authorization") String token) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token.replace("Bearer ", "")) 
                .getBody();

            Long userId = Long.parseLong(claims.get("userId").toString());
            List<ListWithMoviesDTO> userLists = listService.getMovieLists(userId);
            return ResponseEntity.ok(userLists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid token"));
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> createList(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        try {
            System.out.println("Authorization Header: " + token);

            // Elimina el prefijo "Bearer " del token
            String jwtToken = token.replace("Bearer ", "");
            System.out.println("JWT Token: " + jwtToken);
            // Desencripta el token

            Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

            // Imprime todos los claims del token
            System.out.println("Token Claims: " + claims);

            // Extrae el userId
            Long userId = Long.parseLong(claims.get("userId").toString());
            System.out.println("UserId from token: " + userId);

            // Continúa con la lógica de negocio
            String listName = request.get("name");
            listService.createList(userId, listName);
            return ResponseEntity.ok(Collections.singletonMap("message", "lista creada correctamente."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al crear la lista.");
        }
    }
        
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteList(
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {
        try {;

            // Extrae el userId desde el token
            String jwtToken = token.replace("Bearer ", "");
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

            Long userId = Long.parseLong(claims.get("userId").toString());

            // Verifica que 'name' no sea nulo
            String listName = request.get("name");
            if (listName == null || listName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre de la lista es requerido.");
            }

            // Llama al servicio para eliminar la lista
            listService.deleteList(userId, listName);

            return ResponseEntity.ok(Collections.singletonMap("message", "lista eliminada correctamente."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la lista.");
        }
    }

    @DeleteMapping("/delete/movie")
    public ResponseEntity<?> deleteMovieFromList(
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
            Long listId = Long.parseLong(request.get("listId").toString());
            Long movieId = Long.parseLong(request.get("movieId").toString());

            // Llama al servicio para eliminar la película de la lista
            boolean deleted = listService.deleteMovieFromList(listId, movieId);

            if (deleted) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Película eliminada correctamente."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "La película no está en la lista."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la película.");
        }
    }

    


    @PostMapping("/add-movie")
    public ResponseEntity<?> addMovieToList(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
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

            // Obtén el nombre de la lista y el ID de la película del request
            String listName = (String) request.get("listName");
            Long movieId = Long.parseLong(request.get("movieId").toString());

            // Busca la lista por nombre y userId
            ListEntity list = listRepository.findByNameAndUserId(listName, userId)
                    .orElseThrow(() -> new RuntimeException("Lista no encontrada o no autorizado para acceder a ella"));
            
            boolean exists = movieRepository.existsByMovieIdAndList(movieId, list);
            if (exists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "La película ya está en la lista."));
            }
            
            // Lógica para añadir la película a la lista
            MovieList movie = new MovieList();
            movie.setMovieId(movieId);
            movie.setList(list);

            movieRepository.save(movie);

            return ResponseEntity.ok(Collections.singletonMap("message", "Película añadida correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para añadir la película.");
        }
    }

}
