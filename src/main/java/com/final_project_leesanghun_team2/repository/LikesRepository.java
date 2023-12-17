package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Likes;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByUserAndPost(User user, Post post); // 이것도 LikesRepository 에 있는 user, post 정보를 뽑아오는 거야.
    void deleteByUserAndPost(User user, Post post);
    Long countByPost(Post post);
}
