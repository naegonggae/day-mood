package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.post.PostFindResponse;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveResponse;
import com.final_project_leesanghun_team2.domain.dto.post.PostUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveRequest;
import com.final_project_leesanghun_team2.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import lombok.RequiredArgsConstructor;
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
@Api(tags = {"Post API"})
public class PostRestController {

    private final PostService postService;

    // 게시물 등록
    @ApiOperation(value = "Post 추가", notes = "(유효한 jwt Token 필요) title, body 데이터를 저장")
    @PostMapping
    public ResponseEntity<Response<PostSaveResponse>> save(@RequestBody PostSaveRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        PostSaveResponse result = postService.savePost(request, details.getUser());
        return ResponseEntity.created(URI.create("/api/v1/posts/"+result.getId()))
                .body(Response.success(result));
    }

    // 태그별 게시물 조회
//    @ApiOperation(value = "Post 리스트 조회", notes = "작성된 게시글을 최신순으로 20개씩 페이징 해서 가져온다.")
//    @GetMapping
//    public Response<Page<PostFindResponse>> showAllPosts(
//            @PageableDefault(size = 20)
//            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        return Response.success(postService.showAll(pageable));
//    }

    // 내 게시물 조회
    @ApiOperation(value = "마이 피드 기능 (작성한 게시글 모아보기)", notes = "(유효한 jwt Token 필요) 토큰 정보로 작성한 게시글 조회")
    @GetMapping("/me")
    public ResponseEntity<Response<Page<PostFindResponse>>> findMyPost(
                                @PageableDefault(size = 20)
                                @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                @AuthenticationPrincipal PrincipalDetails details) {
        Page<PostFindResponse> result = postService.findMyPost(pageable, details.getUser());
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 게시물 단건 조회
    @ApiOperation(value = "Post 단건 조회", notes = "path variable로 입력한 postId의 상세 정보를 가져온다.")
    @GetMapping("/{id}")
    public ResponseEntity<Response<PostFindResponse>> findOne(@PathVariable Long id) {
        PostFindResponse result = postService.findOne(id);
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 게시물 수정
    @ApiOperation(value = "Post 수정", notes = "(유효한 jwt Token 필요) path variable로 입력한 postId의 Post를 title, body 로 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
            @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        postService.update(id, request, details.getUser());
        return ResponseEntity.noContent().build();
    }

    // 게시물 삭제
    @ApiOperation(value = "Post 삭제", notes = "(유효한 jwt Token 필요) path variable로 입력한 postId의 Post 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails details) {
        postService.delete(id, details.getUser());
        return ResponseEntity.noContent().build();
    }

}
