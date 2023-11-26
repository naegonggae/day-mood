package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Likes;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.ErrorCode;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    /** 좋아요 누르기 **/
    @Transactional
    public String push(Integer postId, Authentication authentication) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // UserName 정보 유무 체크
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        // 좋아요 눌렀었는지 체크
        likesRepository.findByUserAndPost(user, post)
                .ifPresent(item -> {
                    throw new UserSnsException(ErrorCode.ALREADY_LIKED);
                });

        // 좋아요 저장
        likesRepository.save(Likes.of(user, post));

        return "좋아요를 눌렀습니다.";
    }

    /** 좋아요 개수 **/
    public Long show(Integer postId) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // 갯수 뽑아오기
        Long likes = likesRepository.countByPost(post);

        return likes;
    }
}
