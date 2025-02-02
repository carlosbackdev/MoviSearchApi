
package com.carlosbackdev.movieSearch.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;


@Entity
@Table(name = "login")
@Data
public class User {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("id") long id,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email,
                @JsonProperty("nif") String nif) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}