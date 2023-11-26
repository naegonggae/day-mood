package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByUser(User user, Pageable pageable);

    List<Post> findByTitleOrBody(String title, String body);
    Page<Post> findByTitleContainingOrBodyContaining(String title, String body, Pageable pageable);


}
