
package com.carlosbackdev.movieSearch.repository;

import com.carlosbackdev.movieSearch.model.ListEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<ListEntity, Long> {
    List<ListEntity> findByUserId(Long userId);
    Optional<ListEntity> findByNameAndUserId(String name, Long userId);
    void deleteByNameAndUserId(String name, Long userId);
}
