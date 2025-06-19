package com.winnerx0.kiroku.repositories;

import com.winnerx0.kiroku.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findAllUserPosts(@Param("userId") String userId);
}
