
package com.carlosbackdev.movieSearch.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor; 


@Entity
@Table(name = "comments") 
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private long movieId;
    private String comment;
    private int likesCount;
    @Temporal(TemporalType.DATE) 
    private Date date;
    
    @PrePersist
    protected void onCreate() {
        this.date = new Date();
    }    
}