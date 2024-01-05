package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.dto.post.PostDefaultResponse;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.post.PostFindResponse;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveResponse;
import com.final_project_leesanghun_team2.domain.dto.post.request.PostUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.post.request.PostSaveRequest;
import com.final_project_leesanghun_team2.service.PostService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostRestController {

    private final PostService postService;

    // 게시물 등록
    @PostMapping
    public ResponseEntity<Response<PostSaveResponse>> savePost(
            @Valid @RequestBody PostSaveRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("save 시작");
        PostSaveResponse result = postService.savePost(request, details.getUser());
        log.info("save 끝");
        return ResponseEntity.created(URI.create("/api/v1/posts/"+result.getId()))
                .body(Response.success(result));
    }

    // 태그로 검색된 게시물들
    @GetMapping("/tag")
    public ResponseEntity<Response<Page<PostFindResponse>>> findPostsByTag(
            @AuthenticationPrincipal PrincipalDetails details,
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "tagName") String tagName) {
        Page<PostFindResponse> result = postService.findPostsByTag(pageable, tagName, details.getUser().getId());
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 전체 게시물
    @GetMapping
    public ResponseEntity<Response<Page<PostDefaultResponse>>> findAllPosts(
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("findMyPost 시작");
        Page<PostDefaultResponse> result = postService.findAllPosts(pageable);
        log.info("findMyPost 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 로그인한 유저의 게시물들
    @GetMapping("/me")
    public ResponseEntity<Response<Page<PostFindResponse>>> findLoginUserPosts(
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("findMyPost 시작");
        Page<PostFindResponse> result = postService.findLoginUserPosts(pageable, details.getUser());
        log.info("findMyPost 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 유저의 게시물들
    @GetMapping("/users/{id}")
    public ResponseEntity<Response<Page<PostFindResponse>>> findUserPosts(
            @AuthenticationPrincipal PrincipalDetails details,
            @PathVariable Long id,
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("findMyPost 시작");
        Page<PostFindResponse> result = postService.findUserPosts(pageable, id, details.getUser().getId());
        log.info("findMyPost 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 게시물 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Response<PostFindResponse>> findPost(@PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("findByUserId 시작");
        PostFindResponse result = postService.findPost(id, details.getUser());
        log.info("findByUserId 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("update 시작");
        postService.updatePost(id, request, details.getUser());
        log.info("update 끝");
        return ResponseEntity.noContent().build();
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("delete 시작");
        postService.deletePost(id, details.getUser());
        log.info("delete 끝");
        return ResponseEntity.noContent().build();
    }

}