
package com.carlosbackdev.movieSearch.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor; 


@Entity
@Table(name = "chat_history") 
@Data
@NoArgsConstructor
public class ChatHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Column(name = "user_phrase", columnDefinition = "TEXT")
    private String userPhrase;
    @Column(name = "response",columnDefinition = "TEXT")
    private String response;
    @Column(name = "peliculas_id")
    @ElementCollection(targetClass = Long.class)
    private List<Long> peliculasId;
    @Temporal(TemporalType.DATE) 
    private Date date;
    
    @PrePersist
    protected void onCreate() {
        this.date = new Date();
    }
    
    public ChatHistory(Long userId, String userPhrase, String response) {
        this.userId = userId;
        this.userPhrase = userPhrase;
        this.response = response;
        this.date = new Date(); 
    }
    
}
