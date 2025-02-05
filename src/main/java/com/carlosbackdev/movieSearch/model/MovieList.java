
package com.carlosbackdev.movieSearch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 

@Entity
@Table(name = "movies_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    private Long movieId; 
    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    @JsonIgnore
    private ListEntity list;
    
}
