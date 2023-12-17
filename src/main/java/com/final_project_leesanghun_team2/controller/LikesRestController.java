package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.likes.LikesPushResponse;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.service.LikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = {"Like API"})
public class LikesRestController {

    private final LikesService likesService;

    // 좋아요 누르기
    @ApiOperation(value = "좋아요 추가", notes = "(유효한 jwt Token 필요), 한 계정당 한개만 누를 수 있다.")
    @PostMapping("/{postId}/likes")
    public ResponseEntity<Response<LikesPushResponse>> pushLike(@PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails details) {
        LikesPushResponse result = likesService.pushLike(postId, details.getUser());
        return ResponseEntity.ok().body(Response.success(result));
    }
}
