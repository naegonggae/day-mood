package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.request.CommentAddRequest;
import com.final_project_leesanghun_team2.domain.request.CommentModifyRequest;
import com.final_project_leesanghun_team2.domain.response.CommentDeleteResponse;
import com.final_project_leesanghun_team2.domain.response.CommentModifyResponse;
import com.final_project_leesanghun_team2.domain.response.CommentShowResponse;
import com.final_project_leesanghun_team2.service.CommentService;
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
@Api(tags = {"Comment API"})
public class CommentRestController {

    private final CommentService commentService;

    /** 댓글 등록 **/
    @ApiOperation(value = "Comment 작성", notes = "Path variable에 해당하는 포스트에, 입력한 comment 내용을 저장")
    @PostMapping("/{postsId}/comments")
    public Response<CommentShowResponse> addComment(@PathVariable Integer postsId,
                                                    @RequestBody CommentAddRequest commentAddRequest,
                                                    Authentication authentication) {
        return Response.success(commentService.add(postsId, commentAddRequest, authentication));
    }

    /** 댓글 조회 **/
    @ApiOperation(value = "Comment 리스트 조회", notes = "작성된 댓글을 최신순으로 10개씩 페이징 해서 가져온다.")
    @GetMapping("/{postsId}/comments")
    public Response<Page<CommentShowResponse>> showAllComment(@PathVariable Integer postsId, @PageableDefault(size = 10)
                                                             @SortDefault(sort = "createdAt",direction = Sort.Direction.DESC)
                                                             Pageable pageable) {
        return Response.success(commentService.showAll(postsId, pageable));
    }

    /** 댓글 수정 **/
    @ApiOperation(value = "Comment 수정", notes = "(유효한 jwt Token 필요) path variable로 입력한 postId의 Post의 commentId의 내용을 수정")
    @PutMapping("/{postsId}/comments/{id}")
    public Response<CommentModifyResponse> modifyComment(@PathVariable Integer postsId,
                                                         @PathVariable Integer id,
                                                         @RequestBody CommentModifyRequest commentModifyRequest,
                                                         Authentication authentication) {
        return Response.success(commentService.modify(postsId, id, commentModifyRequest ,authentication));
    }

    /** 댓글 삭제 **/
    @ApiOperation(value = "Comment 삭제", notes = "(유효한 jwt Token 필요) path variable로 입력한 postId의 Post의 commentId의 Comment를 삭제")
    @DeleteMapping("/{postsId}/comments/{id}")
    public Response<CommentDeleteResponse> deleteComment(@PathVariable Integer postsId,
                                                         @PathVariable Integer id,
                                                         Authentication authentication) {
        return Response.success(commentService.delete(postsId, id, authentication));
    }
}
