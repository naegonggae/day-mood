package com.final_project_leesanghun_team2.repository.post;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{
    Page<Post> findAllByUser(User user, Pageable pageable);
    Long countAllByUser(User user);
    @Query("SELECT p FROM Post p WHERE p.user IN (SELECT f.followingUser FROM Follow f WHERE f.followUser.id = :userId)")
    Page<Post> findPostsByFollowers(Long userId, Pageable pageable);
    @Query( "SELECT p.user " +
            "FROM Post p " +
            "GROUP BY p.user " +
            "ORDER BY COUNT(p) DESC")
    List<User> findTop5UsersWithMostPosts();
}
