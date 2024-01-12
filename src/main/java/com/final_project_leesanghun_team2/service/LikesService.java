package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.likes.LikesPushResponse;
import com.final_project_leesanghun_team2.domain.entity.Likes;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.post.NoSuchPostException;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    // 좋아요 이벤트
    @Transactional
    public LikesPushResponse pushLike(Long postId, User user) {

        // 게시물
        Post post = postRepository.findById(postId).orElseThrow(NoSuchPostException::new);

        // 게시물의 좋아요 개수
        Long likeCount = likesRepository.countByPost(post);
//        long likeCount = post.getLikeList().size();

        // 로그인한 유저가 좋아요를 누른지 여부
        boolean isLike = likesRepository.existsByUserAndPost(user, post);
//        boolean isLike = post.getLikeList().stream()
//                .anyMatch(like -> like.getUser().getId().equals(user.getId()));

        // 좋아요 있음 -> 좋아요 삭제
        if (isLike) {
            Likes findLike = likesRepository.findByUserAndPost(user, post).orElseThrow(NoSuchPostException::new);
            likesRepository.delete(findLike);
            post.removePost(findLike);
            return LikesPushResponse.of(false, likeCount-1);
        }

        // 좋아요 없음 -> 좋아요 생성
        else {
            Likes likes = Likes.createLikes(user, post);
            likesRepository.save(likes);
            return LikesPushResponse.of(true, likeCount+1);
        }
    }
}