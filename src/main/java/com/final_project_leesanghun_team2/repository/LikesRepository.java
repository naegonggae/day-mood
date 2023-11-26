package com.final_project_leesanghun_team2.repository;

import com.final_project_leesanghun_team2.domain.entity.Likes;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Integer> {

    Optional<Likes> findByUserAndPost(User user, Post post); // 이것도 LikesRepository 에 있는 user, post 정보를 뽑아오는 거야.

    Long countByPost(Post post);
    // Like 개수를 세는거니까 countByLikes(Post post) 라고 해야하는거 아니야 생각할 수 있는데
    // 위의 기능 사용 포멧이 countBy+엔티티명 => 해당엔티티불러온다. 여서 LikesRepository 에 저장된 Post 정보를 가져온다는 접근을 해야한다.
    // 리턴값은 Long 으로 고정

    @Transactional
    @Modifying
    @Query("UPDATE Likes likes SET deleted_at = NOW() where likes.post = :post")
    void deleteAllByPost(@Param("post") Post post);
}
