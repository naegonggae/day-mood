package com.final_project_leesanghun_team2.controller;

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

    // 태그별 게시물 조회
    @GetMapping("/tag")
    public ResponseEntity<Response<Page<PostFindResponse>>> findByTag(
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "tagName") String tagName) {
        Page<PostFindResponse> result = postService.findByTag(pageable, tagName);
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 게시물 전체 조회
    @GetMapping
    public ResponseEntity<Response<Page<PostFindResponse>>> findAllPosts(
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("findMyPost 시작");
        Page<PostFindResponse> result = postService.findAllPosts(pageable);
        log.info("findMyPost 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 내 게시물 전체 조회
    @GetMapping("/me")
    public ResponseEntity<Response<Page<PostFindResponse>>> findMyPosts(
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("findMyPost 시작");
        Page<PostFindResponse> result = postService.findMyPosts(pageable, details.getUser());
        log.info("findMyPost 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // user id 의 게시물 전체 조회
    @GetMapping("/users/{id}")
    public ResponseEntity<Response<Page<PostFindResponse>>> findUserPost(
            @PathVariable Long id,
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("findMyPost 시작");
        Page<PostFindResponse> result = postService.findUserPost(pageable, id);
        log.info("findMyPost 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 게시물 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Response<PostFindResponse>> findOne(@PathVariable Long id) {
        log.info("findOne 시작");
        PostFindResponse result = postService.findOne(id);
        log.info("findOne 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("update 시작");
        postService.update(id, request, details.getUser());
        log.info("update 끝");
        return ResponseEntity.noContent().build();
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("delete 시작");
        postService.delete(id, details.getUser());
        log.info("delete 끝");
        return ResponseEntity.noContent().build();
    }

}