
package com.carlosbackdev.movieSearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListWithMoviesDTO {
    private Long id;
    private String name;
    private List<MovieDTO> movies;
}