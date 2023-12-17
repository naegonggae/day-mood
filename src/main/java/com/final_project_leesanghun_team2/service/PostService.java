package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.dto.post.request.PostSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.post.request.PostUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.post.PostFindResponse;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveResponse;
import com.final_project_leesanghun_team2.domain.entity.Tag;
import com.final_project_leesanghun_team2.domain.entity.TagPost;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.post.NoSuchPostException;
import com.final_project_leesanghun_team2.exception.tag.NoSuchTagException;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.exception.user.PermissionDeniedException;
import com.final_project_leesanghun_team2.repository.CommentRepository;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.TagPostRepository;
import com.final_project_leesanghun_team2.repository.TagRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TagPostRepository tagPostRepository;

    // 게시물 등록
    @Transactional
    public PostSaveResponse savePost(PostSaveRequest request, User user) {

        // post 객체 생성
        Post post = Post.createPost(request, user);

        // String tagName list -> TagPost list 로 변환
        List<TagPost> tagPostList = getTagPostList(request.getTagList(), post);

        // post 에 적용
        post.addTagPost(tagPostList);

        Post savedPost = postRepository.save(post);

        return PostSaveResponse.from(savedPost);
    }

    // 태그 별 게시물
    public Page<PostFindResponse> findByTag(Pageable pageable, String tagName) {

        Tag tag = tagRepository.findByName(tagName).orElseThrow(NoSuchTagException::new);

        return tagPostRepository.findAllByTag(tag, pageable)
                .map(tagPost -> new PostFindResponse(tagPost.getPost()));
    }

    // 게시물 전체 조회
    public Page<PostFindResponse> findAllPosts(Pageable pageable) {

        return postRepository.findAll(pageable).map(PostFindResponse::new);
    }

    // 1. 나의 게시물 전체 조회 -> UserDetail 의 user id
    public Page<PostFindResponse> findMyPosts(Pageable pageable, User user) {
        return postRepository.findAllByUser(user, pageable).map(PostFindResponse::new);
    }

    // 2. 나의 게시물 전체 조회 -> user id 를 받음
    public Page<PostFindResponse> findUserPost(Pageable pageable, Long id) {

        User findUser = userRepository.findById(id).orElseThrow(NoSuchUserException::new);
        return postRepository.findAllByUser(findUser, pageable).map(PostFindResponse::new);
    }

    // 게시물 단건 조회
    public PostFindResponse findOne(Long id) {

        Post findPost = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        return new PostFindResponse(findPost);
    }

    // 내 개시물 수정
    @Transactional
    public void update(Long id, PostUpdateRequest request, User user) {

        Post post = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // Post-User == Login-User 일 때 수정
        checkPermission(user, post);

        // 수정된 String tagName list -> TagPost list 로 변환
        List<TagPost> tagPostList = getTagPostList(request.getTagList(), post);

        post.update(request, tagPostList);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long id, User user) {

        Post post = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // Post-User == Login-User 일 때 삭제
        checkPermission(user, post);

        // cascade remove 와 orphanRemoval = true 로 tagPost 자동 삭제

        // 댓글, 좋아요 삭제하고 포스트 삭제 진행
//        likesRepository.deleteAllByPost(post);
//        commentRepository.deleteAllByPost(post);

        postRepository.delete(post);
    }

    private static void checkPermission(User user, Post post) {
        if (!Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            throw new PermissionDeniedException();
        }
    }

    private List<TagPost> getTagPostList(List<String> request, Post post) {
        List<TagPost> tagPostList = request.stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(NoSuchTagException::new))
                .map(tag -> TagPost.createTagPost(post, tag))
                .collect(Collectors.toList());
        return tagPostList;
    }
}