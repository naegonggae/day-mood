package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.post.PostUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.post.PostFindResponse;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveResponse;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.post.NoSuchPostException;
import com.final_project_leesanghun_team2.exception.user.PermissionDeniedException;
import com.final_project_leesanghun_team2.repository.CommentRepository;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시물 등록
    @Transactional
    public PostSaveResponse savePost(PostSaveRequest request, User user) {

        Post post = Post.createPost(request, user);
        Post savedPost = postRepository.save(post);

        return PostSaveResponse.from(savedPost);
    }

    // 태그 검색 결과 게시물들 조회
    // 이거 쿼리 dsl 로 하자
//    public Page<PostFindResponse> findByTag(Pageable pageable) {
//
//        // 포스트 전부 가져오기
//        Page<Post> posts = postRepository.findAll(pageable);
//
//        // PostFindResponse 리스트에 DB에서 찾아온 Post 리스트자료를 각각 대입
//        return PostFindResponse.toList(posts);
//    }

    // 내 게시물 전체 조회
    public Page<PostFindResponse> findMyPost(Pageable pageable, User user) {

        return postRepository.findAllByUser(user, pageable).map(PostFindResponse::from);
    }

    // 게시물 단건 조회
    public PostFindResponse findOne(Long id) {

        Post findPost = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        return PostFindResponse.from(findPost);
    }

    // 내 개시물 수정
    @Transactional
    public void update(Long id, PostUpdateRequest request, User user) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // 해당 postId의 포스트에 저장된 Id와 접속하려는 User 의 Id가 일치하는가 // 권한이 있는가
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new PermissionDeniedException();
        }

        post.update(request);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long id, User user) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // 해당 postId의 포스트에 저장된 UserName 과 삭제하려는 User 의 UserName 이 일치하는가 ;권한이 있는가
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new PermissionDeniedException(); }

        // 이거를 cascade 와 open 으로 해봅시다.
        // 해당 postId의 포스트 삭제하기 // 포스트 삭제시 해당 글의 댓글과 좋아요 삭제처리
//        likesRepository.deleteAllByPost(post);
//        commentRepository.deleteAllByPost(post);
        postRepository.delete(post);
    }

}
