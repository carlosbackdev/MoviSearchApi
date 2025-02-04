
package com.carlosbackdev.movieSearch.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor; 


@Entity
@Table(name = "user") // Nombre de la tabla en la base de datos
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    @Temporal(TemporalType.DATE) 
    private Date date;
    
    @PrePersist
    protected void onCreate() {
        this.date = new Date();
    }    
}