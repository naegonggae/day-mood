package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.request.PostAddRequest;
import com.final_project_leesanghun_team2.domain.request.PostModifyRequest;
import com.final_project_leesanghun_team2.domain.response.PostResultResponse;
import com.final_project_leesanghun_team2.domain.response.PostShowResponse;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.ErrorCode;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import com.final_project_leesanghun_team2.repository.CommentRepository;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /** 포스트 등록 **/
    @Transactional
    public PostResultResponse add(PostAddRequest postAddRequest, Authentication authentication) {

        // UserName 정보 유무 체크
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        // 포스트 정보 DB에 저장
        Post savedPost = postRepository.save(Post.of(postAddRequest.getTitle(), postAddRequest.getBody(), user));

        // PostResultResponse 에 DB에 저장된 포스트 id 연결
        return PostResultResponse.ofPost(savedPost.getId());
    }

    /** 포스트 리스트 **/
    @Transactional(readOnly = true)
    public Page<PostShowResponse> showAll(Pageable pageable) {

        // 포스트 전부 가져오기
        Page<Post> posts = postRepository.findAll(pageable);

        // PostShowResponse 리스트에 DB에서 찾아온 Post 리스트자료를 각각 대입
        return PostShowResponse.toList(posts);
    }

    /** 포스트 1개 조회 **/
    @Transactional(readOnly = true)
    public PostShowResponse showOne(Integer postId) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // Post -> PostShowResponse 로 포장
        return PostShowResponse.of(post);
    }

    /** 포스트 수정 **/
    @Transactional
    public PostResultResponse modify(Integer postId, PostModifyRequest postModifyRequest, Authentication authentication) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // UserName 정보 유무 체크
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        // 해당 postId의 포스트에 저장된 Id와 접속하려는 User 의 Id가 일치하는가 // 권한이 있는가
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new UserSnsException(ErrorCode.INVALID_PERMISSION);
        }

        // 수정된 title, body 연결 // 이거 좀 간단하게 못하나 메소드로 묶는다거나
        post.setTitle(postModifyRequest.getTitle());
        post.setBody(postModifyRequest.getBody());
        Post savedPost = postRepository.save(post);

        return PostResultResponse.ofModifyPost(savedPost.getId());

        // 나는 또 수정할수도 있으니까 save 사용해야한다 생각하는데 강사님은 saveAndFlush 사용함 나중에 이유 찾아보기
        // 1. save() 메소드는 영속성 컨텍스트에 저장하는 것이고 실제로 DB 에 저장은 추후 flush 또는 commit 메소드가 실행될 때 이루어짐
        // 2. saveAndFlush() 메소드는 즉시 DB 에 데이터를 반영함
    }

    /** 포스트 삭제 **/
    @Transactional
    public PostResultResponse delete(Integer postId, Authentication authentication) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UserSnsException(ErrorCode.POST_NOT_FOUND));

        // UserName 정보 유무 체크
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        // 해당 postId의 포스트에 저장된 UserName 과 삭제하려는 User 의 UserName 이 일치하는가 ;권한이 있는가
        if (!Objects.equals(post.getUser().getUserName(), authentication.getName())) {
            throw new UserSnsException(ErrorCode.INVALID_PERMISSION); }

        // 해당 postId의 포스트 삭제하기 // 포스트 삭제시 해당 글의 댓글과 좋아요 삭제처리
        likesRepository.deleteAllByPost(post);
        commentRepository.deleteAllByPost(post);
        postRepository.delete(post);

        return PostResultResponse.ofDeletePost(post.getId());
    }

    /** 마이페이지 조회 **/
    public Page<PostShowResponse> showMy(Pageable pageable, Authentication authentication) {

        // UserName 정보 유무 체크
        User user = userRepository.findByUserName(authentication.getName()).orElseThrow(() ->
                new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        // postRepository 에 저장되어있는 user 정보들 Page<> 형태로 가져오기
        return postRepository.findAllByUser(user, pageable).map(PostShowResponse::of);
        // x -> PostShowResponse.of 이거를 줄인듯
    }
}
