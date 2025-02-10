
package com.carlosbackdev.movieSearch.repository;

import com.carlosbackdev.movieSearch.model.ListEntity;
import com.carlosbackdev.movieSearch.model.MovieList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<ListEntity, Long> {
    List<ListEntity> findByUserId(Long userId);
    Optional<ListEntity> findByNameAndUserId(String name, Long userId);
    Optional<ListEntity> findByIdAndUserId(Long id, Long userId);
    void deleteByNameAndUserId(String name, Long userId);
}
