package com.final_project_leesanghun_team2.repository.post;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{
    Page<Post> findAllByUser(User user, Pageable pageable);
    Long countAllByUser(User user);

}
