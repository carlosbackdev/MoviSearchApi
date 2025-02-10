
package com.carlosbackdev.movieSearch.repository;

import com.carlosbackdev.movieSearch.model.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    boolean existsByUserIdAndListId(Long userId, Long listId);
}