package com.carlosbackdev.movieSearch.repository;

import com.carlosbackdev.movieSearch.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); 
    User findByUsername(String username);
    User findByFirebaseUid(String firebaseUid);
}