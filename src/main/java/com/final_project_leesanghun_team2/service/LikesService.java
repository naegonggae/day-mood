package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.likes.LikesCheckResponse;
import com.final_project_leesanghun_team2.domain.dto.likes.LikesPushResponse;
import com.final_project_leesanghun_team2.domain.entity.Likes;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.post.NoSuchPostException;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    // 좋아요 누르기, 취소하기
    @Transactional
    public LikesPushResponse pushLike(Long postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);

        // 게시물에 좋아요 개수
        Long count = likesRepository.countByPost(post);

        // 좋아요 눌렀었는지 체크
        boolean hasLiked = likesRepository.existsByUserAndPost(user, post);


        // 좋아요 누른 상태 -> 좋아요를 삭제
        if (hasLiked) {
            likesRepository.deleteByUserAndPost(user, post);
            post.decreaseLikeCount();
            post.getUser().decreaseTotalLike();
            return LikesPushResponse.from(false, count-1);
        }
        // 좋아요 아직 안 누른 상태 -> 좋아요를 생성
        else {
            Likes likes = Likes.createLikes(user, post);
            likesRepository.save(likes);
            post.increaseLikeCount();
            post.getUser().increaseTotalLike();
            return LikesPushResponse.from(true, count+1);
        }
    }

    // 유저가 좋아요를 눌렀는지 체크
    public LikesCheckResponse hasUserLikedPost(Long postId, Long userId) {

        User findUser = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchPostException::new);

        // 좋아요 눌렀었는지 체크
        boolean hasLiked = likesRepository.existsByUserAndPost(findUser, findPost);

        return LikesCheckResponse.from(hasLiked);
    }
}
