
package com.carlosbackdev.movieSearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private Long movieId;  // ID de la película original
}