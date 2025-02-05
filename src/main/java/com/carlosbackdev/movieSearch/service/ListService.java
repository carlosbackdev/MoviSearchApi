
package com.carlosbackdev.movieSearch.service;

import com.carlosbackdev.movieSearch.model.ListEntity;
import com.carlosbackdev.movieSearch.model.MovieList;
import com.carlosbackdev.movieSearch.model.User;
import com.carlosbackdev.movieSearch.repository.ListRepository;
import com.carlosbackdev.movieSearch.repository.MovieRepository;
import java.util.*;
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
}