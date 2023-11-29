package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.post.PostUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.post.PostFindResponse;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveResponse;
import com.final_project_leesanghun_team2.domain.entity.Tag;
import com.final_project_leesanghun_team2.domain.entity.TagPost;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.post.NoSuchPostException;
import com.final_project_leesanghun_team2.exception.tag.NoSuchTagException;
import com.final_project_leesanghun_team2.exception.user.PermissionDeniedException;
import com.final_project_leesanghun_team2.repository.CommentRepository;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.TagPostRepository;
import com.final_project_leesanghun_team2.repository.TagRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
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

        // 태그는 이미 만들어져 있음

        Post post = Post.createPost(request, user);

        List<String> tagList = request.getTagList();

        List<TagPost> tagPostList = new ArrayList<>();

        for (String tag : tagList) {
            Tag findTag = tagRepository.findByName(tag).orElseThrow(NoSuchTagException::new);
            TagPost tagPost = TagPost.createTagPost(post, findTag); // cascade 조건으로 이렇게만 해놓으면 DB에 저장됨
            tagPostList.add(tagPost);
        }
        post.addTagPost(tagPostList);

//        Set<String> uniqueTags = new HashSet<>(tagList);
//        if (uniqueTags.size() < tagList.size()) {
//            // 중복된 태그가 있다면 처리
//            throw new DuplicateTagException("중복된 태그가 포함되어 있습니다.");
//        }
//
//        List<TagPost> tagPostList = uniqueTags.stream()
//                .map(tagName -> tagRepository.findByName(tagName)
//                        .orElseThrow(() -> new NoSuchTagException("태그를 찾을 수 없습니다: " + tagName)))
//                .map(tag -> TagPost.createTagPost(post, tag))
//                .collect(Collectors.toList());
//        post.addTagPost(tagPostList);

        Post savedPost = postRepository.save(post);

        return PostSaveResponse.from(savedPost);
    }

    // 태그 검색 결과 게시물들 조회 -> 정렬할 기준이 필요..
    public Page<PostFindResponse> findByTag(Pageable pageable, String tagName) {

        Tag tag = tagRepository.findByName(tagName).orElseThrow(NoSuchTagException::new);

        Page<TagPost> tagPosts = tagPostRepository.findAllByTag(tag, pageable);

        return tagPosts.map(tagPost -> PostFindResponse.from(tagPost.getPost()));
    }

    // 내 게시물 전체 조회 -> 공개여부 상관없음
    public Page<PostFindResponse> findMyPost(Pageable pageable, User user) {

        return postRepository.findAllByUser(user, pageable).map(PostFindResponse::from);
    }

    // 게시물 단건 조회
    public PostFindResponse findOne(Long id) {

        Post findPost = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // 내꺼 아니면 공개여부에 따라 못열어
        // 내꺼면 공개여부 상관없이 열어
        return PostFindResponse.from(findPost);
    }

    // 내 개시물 수정
    @Transactional
    public void update(Long id, PostUpdateRequest request, User user) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // 태그를 수정할 수 있어

        // 해당 postId의 포스트에 저장된 Id와 접속하려는 User 의 Id가 일치하는가 // 권한이 있는가
        if (!Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            throw new PermissionDeniedException();
        }

        List<String> tagList = request.getTagList();

        List<TagPost> tagPostList = new ArrayList<>();

        for (String tag : tagList) {
            Tag findTag = tagRepository.findByName(tag).orElseThrow(NoSuchTagException::new);
            TagPost tagPost = TagPost.createTagPost(post, findTag); // cascade 조건으로 이렇게만 해놓으면 DB에 저장됨
            tagPostList.add(tagPost);
        }
//        post.addTagPost(tagPostList);

        post.update(request, tagPostList);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long id, User user) {

        // 해당 postId의 포스트 유무 체크
        Post post = postRepository.findById(id)
                .orElseThrow(NoSuchPostException::new);

        // 해당 postId의 포스트에 저장된 UserName 과 삭제하려는 User 의 UserName 이 일치하는가 ;권한이 있는가
        if (!Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            throw new PermissionDeniedException(); }

        // cascade remove 와 orphanRemoval = true 로 tagPost 자동 삭제

        // 댓글, 좋아요 삭제하고 포스트 삭제 진행
//        likesRepository.deleteAllByPost(post);
//        commentRepository.deleteAllByPost(post);

        postRepository.delete(post);
    }

}
