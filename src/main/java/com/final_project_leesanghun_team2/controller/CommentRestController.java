package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.comment.request.CommentSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.CommentSaveResponse;
import com.final_project_leesanghun_team2.domain.dto.comment.request.CommentUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.CommentFindResponse;
import com.final_project_leesanghun_team2.domain.dto.comment.ReplyFindResponse;
import com.final_project_leesanghun_team2.domain.dto.comment.request.ReplySaveRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.ReplySaveResponse;
import com.final_project_leesanghun_team2.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = {"Comment API"})
public class CommentRestController {

    private final CommentService commentService;

    // 댓글 등록
    @ApiOperation(value = "Comment 작성", notes = "Path variable에 해당하는 포스트에, 입력한 comment 내용을 저장")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Response<CommentSaveResponse>> saveComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentSaveRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        CommentSaveResponse result = commentService.saveComment(postId, request, details.getUser());
        return ResponseEntity.created(URI.create("/api/v1/posts/"+postId+"/comments/"+result.getId()))
                .body(Response.success(result));
    }

    // 답글 등록
    @PostMapping("/{postId}/comments/{prtId}/replies")
    public ResponseEntity<Response<ReplySaveResponse>> saveReply(
            @PathVariable Long prtId, @PathVariable Long postId,
            @Valid @RequestBody ReplySaveRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        ReplySaveResponse result = commentService.saveReply(prtId, postId, request, details.getUser());
        return ResponseEntity
                .created(URI.create("/api/v1/posts/"+postId+"/comments/"+prtId+"/replies/"+result.getId()))
                .body(Response.success(result));
    }

    // 댓글 조회
    @ApiOperation(value = "Comment 리스트 조회", notes = "작성된 댓글을 최신순으로 10개씩 페이징 해서 가져온다.")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Response<Page<CommentFindResponse>>> findAllComments(
            @PathVariable Long postId,
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt",direction = Direction.DESC) Pageable pageable) {
        Page<CommentFindResponse> result = commentService.findAllComments(postId, pageable);
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 답글 조회
    @GetMapping("/{postId}/comments/{prtId}/replies")
    public ResponseEntity<Response<Page<ReplyFindResponse>>> findAllReplies(
            @PathVariable Long postId, @PathVariable Long prtId,
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt",direction = Direction.DESC) Pageable pageable) {
        Page<ReplyFindResponse> result = commentService.findAllReplies(prtId, postId, pageable);
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 댓글 수정
    @ApiOperation(value = "Comment 수정", notes = "(유효한 jwt Token 필요) path variable로 입력한 postId의 Post의 commentId의 내용을 수정")
    @PutMapping("/{postId}/comments/{cmtId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long postId, @PathVariable Long cmtId,
            @Valid @RequestBody CommentUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        commentService.updateComment(postId, cmtId, request, details.getUser());
        return ResponseEntity.noContent().build();
    }

    // 댓글 삭제
    @ApiOperation(value = "Comment 삭제", notes = "(유효한 jwt Token 필요) path variable로 입력한 postId의 Post의 commentId의 Comment를 삭제")
    @DeleteMapping("/{postId}/comments/{cmtId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId, @PathVariable Long cmtId,
            @AuthenticationPrincipal PrincipalDetails details) {
        commentService.deleteComment(postId, cmtId, details.getUser());
        return ResponseEntity.noContent().build();
    }
}