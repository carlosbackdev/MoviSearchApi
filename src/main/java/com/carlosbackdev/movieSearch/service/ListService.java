
package com.carlosbackdev.movieSearch.service;

import com.carlosbackdev.movieSearch.dto.ListWithMoviesDTO;
import com.carlosbackdev.movieSearch.dto.MovieDTO;
import com.carlosbackdev.movieSearch.model.ListEntity;
import com.carlosbackdev.movieSearch.model.MovieList;
import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.repository.ListRepository;
import com.carlosbackdev.movieSearch.repository.MovieRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ListService {

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<ListEntity> getUserLists(Long userId) {
        return listRepository.findByUserId(userId);
    }
    
    public List<ListWithMoviesDTO> getMovieLists(Long userId) {
        List<ListEntity> userLists = listRepository.findByUserId(userId);

        List<ListWithMoviesDTO> result = new ArrayList<>();
        for (ListEntity list : userLists) {
            List<MovieList> movies = movieRepository.findByList(list);
            List<MovieDTO> movieDTOs = movies.stream()
                .map(movie -> new MovieDTO(movie.getId(), movie.getMovieId()))
                .collect(Collectors.toList());

            result.add(new ListWithMoviesDTO(list.getId(), list.getName(), movieDTOs));
        }

        return result;
    }


    public void createList(Long userId, String listName) {
        ListEntity newList = new ListEntity();
        newList.setName(listName);

        User user = new User();
        user.setId(userId); 
        newList.setUser(user);

        listRepository.save(newList);
    }
    
    public void deleteList(Long userId, String listName) {
    listRepository.findByNameAndUserId(listName, userId).ifPresent(listRepository::delete);
    }

    // Añadir una película a una lista
    public void addMovieToList(Long listId, Long movieId) {
        ListEntity list = listRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Lista no encontrada"));

        MovieList movie = new MovieList();
        movie.setMovieId(movieId);
        movie.setList(list);

        movieRepository.save(movie);
    }
    
    public boolean deleteMovieFromList(Long listId, Long movieId) {
        Optional<MovieList> movieToDelete = movieRepository.findByMovieIdAndListId(movieId, listId);

        if (movieToDelete.isPresent()) {
            movieRepository.delete(movieToDelete.get());
            return true;
        }
        return false;
    }

}