
package com.carlosbackdev.movieSearch.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor; 

@Entity
@Table(name = "like_comments") 
@Data
@NoArgsConstructor
public class LikeComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long userId;
    private long listId;
    @Temporal(TemporalType.DATE) 
    private Date date;
    
    @PrePersist
    protected void onCreate() {
        this.date = new Date();
    }    
    
}
