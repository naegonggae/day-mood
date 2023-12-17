package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import com.final_project_leesanghun_team2.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostAndParentNull(Post post, Pageable pageable);
    Page<Comment> findAllByPostAndParent(Post post, Comment parentComment, Pageable pageable);

}
