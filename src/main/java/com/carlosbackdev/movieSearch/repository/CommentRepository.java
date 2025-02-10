
package com.carlosbackdev.movieSearch.repository;

import com.carlosbackdev.movieSearch.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieId(Long movieId);
}
