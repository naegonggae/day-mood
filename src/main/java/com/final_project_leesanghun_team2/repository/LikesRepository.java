package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Likes;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    Long countByPost(Post post);
}
