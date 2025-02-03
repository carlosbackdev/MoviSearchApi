package com.carlosbackdev.movieSearch.repository;

import com.carlosbackdev.movieSearch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // Buscar por email
    User findByUsername(String username); // Buscar por username (opcional)
}