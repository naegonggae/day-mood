package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import com.final_project_leesanghun_team2.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByPost(Post post, Pageable pageable);
    // 이거 어떻게 만드는건데

    @Transactional
    @Modifying
    @Query("UPDATE Comment comment SET deleted_at = NOW() where comment.post = :post")
    void deleteAllByPost(@Param("post") Post post); // soft delete
}
