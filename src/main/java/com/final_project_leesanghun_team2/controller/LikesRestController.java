package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.service.LikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = {"Like API"})
public class LikesRestController {

    private final LikesService likesService;

    /** 좋아요 누르기 **/
    @ApiOperation(value = "좋아요 추가", notes = "(유효한 jwt Token 필요), 한 계정당 한개만 누를 수 있다.")
    @PostMapping("/{postId}/likes")
    public Response<String> pushLikes(@PathVariable Integer postId, Authentication authentication) {
        return Response.success(likesService.push(postId, authentication));
    }

    /** 좋아요 개수 **/
    @ApiOperation(value = "좋아요 개수 구하기", notes = "해당하는 postId의 좋아요 개수를 출력한다.")
    @GetMapping("/{postId}/likes")
    public Response<Long> showLikes(@PathVariable Integer postId) {
        return Response.success(likesService.show(postId));
    }
}
