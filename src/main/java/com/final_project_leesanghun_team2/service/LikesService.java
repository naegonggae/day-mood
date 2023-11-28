package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Likes;
import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.post.NoSuchPostException;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    // 좋아요 누르기, 취소하기
    @Transactional
    public void push(Long postId, User user) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postId)
                .orElseThrow(NoSuchPostException::new);
        
        // 좋아요 눌렀었는지 체크
        boolean hasLiked = likesRepository.existsByUserAndPost(user, post);

        if (hasLiked) {
            likesRepository.deleteByUserAndPost(user, post);
            post.decreaseLikeCount();
        }
        else {
            Likes likes = Likes.createLikes(user, post);
            likesRepository.save(likes);
            post.increaseLikeCount();
        }
    }

}
