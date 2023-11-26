package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.request.PostModifyRequest;
import com.final_project_leesanghun_team2.domain.request.PostAddRequest;
import com.final_project_leesanghun_team2.domain.response.*;
import com.final_project_leesanghun_team2.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = {"Post API"})
public class PostRestController {

    private final PostService postService;

    /** 포스트 등록 **/
    @ApiOperation(value = "Post 추가", notes = "(유효한 jwt Token 필요) title, body 데이터를 저장")
    @PostMapping
    public Response<PostResultResponse> addPost(@RequestBody PostAddRequest postAddRequest, Authentication authentication) {
        return Response.success(postService.add(postAddRequest, authentication));
    }

    /** 포스트 조회 **/
    @ApiOperation(value = "Post 리스트 조회", notes = "작성된 게시글을 최신순으로 20개씩 페이징 해서 가져온다.")
    @GetMapping
    public Response<Page<PostShowResponse>> showAllPosts(
            @PageableDefault(size = 20)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(postService.showAll(pageable));
    }

    /** 포스트 1개 조회 **/
    @ApiOperation(value = "Post 단건 조회", notes = "path variable로 입력한 postId의 상세 정보를 가져온다.")
    @GetMapping("/{postId}")
    public Response<PostShowResponse> showOnePost(@PathVariable Integer postId) {
        return Response.success(postService.showOne(postId));
    }

    /** 포스트 수정 **/
    @ApiOperation(value = "Post 수정", notes = "(유효한 jwt Token 필요) path variable로 입력한 postId의 Post를 title, body 로 수정")
    @PutMapping("/{id}")
    public Response<PostResultResponse> modifyPost(@PathVariable Integer id,
                                                   @RequestBody PostModifyRequest postModifyRequest,
                                                   Authentication authentication) {
        return Response.success(postService.modify(id, postModifyRequest, authentication));
    }

    /** 포스트 삭제 **/
    @ApiOperation(value = "Post 삭제", notes = "(유효한 jwt Token 필요) path variable로 입력한 postId의 Post 삭제")
    @DeleteMapping("/{postId}")
    public Response<PostResultResponse> delete(@PathVariable Integer postId, Authentication authentication) {
        return Response.success(postService.delete(postId, authentication));
    }

    /** 마이페이지 조회 **/
    @ApiOperation(value = "마이 피드 기능 (작성한 게시글 모아보기)", notes = "(유효한 jwt Token 필요) 토큰 정보로 작성한 게시글 조회")
    @GetMapping("/my")
    public Response<Page<PostShowResponse>> showMyPosts(
                                @PageableDefault(size = 20)
                                @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                Authentication authentication) {
        return Response.success(postService.showMy(pageable, authentication));
    }
}
