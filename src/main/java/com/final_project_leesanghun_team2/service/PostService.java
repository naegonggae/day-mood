package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.post.PostDefaultResponse;
import com.final_project_leesanghun_team2.domain.entity.Comment;
import com.final_project_leesanghun_team2.domain.entity.Likes;
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
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.TagPostRepository;
import com.final_project_leesanghun_team2.repository.comment.CommentRepository;
import com.final_project_leesanghun_team2.repository.post.PostRepository;
import com.final_project_leesanghun_team2.repository.TagRepository;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TagPostRepository tagPostRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final CacheEvictService cacheEvictService;
    private final CacheMethodService cacheMethodService;

    // 게시물 등록
    @Transactional
    public PostSaveResponse savePost(PostSaveRequest request, User user) {

        // post 생성
        Post post = Post.createPost(request, user);

        // List<String> -> List<TagPost>
        List<TagPost> tagPostList = getTagPostList(request.getTagList(), post);

        // List<TagPost> 추가
        post.addTagPost(tagPostList);

        // post 저장
        Post savedPost = postRepository.save(post);

        // tag_id 와 user_id 에 대한 게시물들 캐시 삭제
        cacheEvictService.evictUserPosts(user.getId());
//        cacheEvictService.evictTagPosts(post.getTagPostList());

        return PostSaveResponse.from(savedPost);
    }

    // 태그가 사용된 게시물들
    public Page<PostFindResponse> findPostsByTag(Pageable pageable, String tagName, Long userId) {

        // 태그
        Tag findTag = tagRepository.findByName(tagName).orElseThrow(NoSuchTagException::new);

        // 로그인한 유저
        User findUser = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

        // 태그가 사용된 게시물들 : 캐싱데이터
        List<TagPost> tagPostList = cacheMethodService.getTagPosts(findTag);

        // tagPost 마다 게시물의 좋아요 개수, 로그인한 유저가 좋아요 눌렀는지 여부 추가
        List<PostFindResponse> postFindResponses = tagPostList.stream()
                .map(tagPost -> new PostFindResponse(tagPost.getPost(), findUser))
                .collect(Collectors.toList());

        // List -> Page
        return new PageImpl<>(postFindResponses, pageable, pageable.getPageSize());
    }

    // 게시물 전체 조회
    public Page<PostDefaultResponse> findAllPosts(Pageable pageable) {
        return postRepository.findAllDefaultPost(pageable)
                .map(post -> new PostDefaultResponse(post));
    }

    // 로그인 이후 게시물 전체 조회
    public Page<PostFindResponse> findAllPostsWhenLogin(Pageable pageable, Long userId) {

        // 로그인 유저
        User findUser = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

        return postRepository.findAllPost(pageable)
                .map(post -> new PostFindResponse(post, findUser));
    }

    // 로그인한 유저의 게시물들
    public Page<PostFindResponse> findLoginUserPosts(Pageable pageable, User user) {

        // 로그인한 유저의 게시물들 : 캐싱데이터
        Page<Post> posts = cacheMethodService.getMyPosts(pageable, user);

        // 자기자신의 게시물에 좋아요를 눌렀는지 여부, 게시물의 좋아요 개수 추가
        return posts.map(post -> new PostFindResponse(post, user));
    }

    // 유저의 게시물들
    public Page<PostFindResponse> findUserPosts(Pageable pageable, Long userId, Long loginUserId) {

        // 유저
        User findUser = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

        // 로그인한 유저
        User loginUser = userRepository.findById(loginUserId).orElseThrow(NoSuchUserException::new);

        // 유저의 게시물들 : 캐싱데이터
        Page<Post> posts = cacheMethodService.getUserPosts(pageable, findUser);

        // 로그인한 유저가 유저의 게시물에 좋아요를 눌렀는지 여부, 게시물의 좋아요 개수 추가
        return posts.map(post -> new PostFindResponse(post, loginUser));

    }

    // 게시물 단건 조회
    public PostFindResponse findPost(Long id, User user) {

        // 로그인한 유저
        User loginUser = userRepository.findById(user.getId()).orElseThrow(NoSuchUserException::new);

        // 게시물
        Post findPost = postRepository.findById(id).orElseThrow(NoSuchPostException::new);

        return new PostFindResponse(findPost, loginUser);
    }

    // 개시물 수정
    @Transactional
    public void updatePost(Long id, PostUpdateRequest request, User user) {

        // 게시물
        Post post = postRepository.findById(id).orElseThrow(NoSuchPostException::new);

        // Post-User == Login-User 일 때 수정
        checkPermission(user, post);

        // List<String> -> List<TagPost>
        List<TagPost> tagPostList = getTagPostList(request.getTagList(), post);

        // tag_id 와 user_id 에 대한 게시물들 캐시 삭제
        cacheEvictService.evictUserPosts(user.getId());
//        cacheEvictService.evictTagPosts(post.getTagPostList());

        // 변경감지 수정
        post.update(request, tagPostList);
    }

    // 게시물 삭제
    @Transactional
    public void deletePost(Long id, User user) {

        // 게시물
        Post post = postRepository.findById(id).orElseThrow(NoSuchPostException::new);

        // Post-User == Login-User 일 때 삭제
        checkPermission(user, post);

        // tag_id 와 user_id 에 대한 게시물들 캐시 삭제
        cacheEvictService.evictUserPosts(user.getId());
//        cacheEvictService.evictTagPosts(post.getTagPostList());

        // post 에 달린 comment, tagPost, likes 논리적 삭제
        List<Comment> comments = commentRepository.findAllByPost(post);
        for (Comment comment : comments) comment.softDelete();
        List<TagPost> tagPosts = tagPostRepository.findAllByPost(post);
        for (TagPost tagPost : tagPosts) tagPost.softDelete();
        List<Likes> likes = likesRepository.findAllByPost(post);
        for (Likes like : likes) like.softDelete();

        // post 는 논리적 삭제
        post.softDelete();
        log.info("post-id : {}를 삭제했습니다.", post.getId());
    }

    private void checkPermission(User user, Post post) {
        if (!Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            throw new PermissionDeniedException();
        }
    }

    private List<TagPost> getTagPostList(List<String> tagList, Post post) {
        List<TagPost> tagPostList = tagList.stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findByName(tagName).orElseThrow(NoSuchTagException::new);
                    return TagPost.createTagPost(post, tag);
                })
                .collect(Collectors.toList());
        return tagPostList;
    }
}