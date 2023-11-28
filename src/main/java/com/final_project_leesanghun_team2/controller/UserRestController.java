package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.dto.user.TokenResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserFindResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserJoinRequest;
import com.final_project_leesanghun_team2.domain.dto.user.UserJoinResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserLoginRequest;
import com.final_project_leesanghun_team2.domain.dto.user.UserUpdateRequest;
import com.final_project_leesanghun_team2.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Api(tags = {"User API"})
public class UserRestController {

    private final UserService userService;

    // 회원가입
    @ApiOperation(value = "회원 가입", notes = "userName, password로 회원 데이터 저장")
    @PostMapping("/join")
    public ResponseEntity<Response<UserJoinResponse>> join(@RequestBody UserJoinRequest request) {
        UserJoinResponse result = userService.join(request);
        return ResponseEntity.created(URI.create("/api/v1/users/join"+result.getId()))
                .body(Response.success(result));
    }

    // 로그인
    @ApiOperation(value = "회원 로그인", notes = "userName, password로 저장된 회원 데이터가 있으면 jwt Token 반환")
    @PostMapping("/login")
    public ResponseEntity<Response<TokenResponse>> login(@RequestBody UserLoginRequest request) {
        TokenResponse result = userService.login(request);
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 회원이름으로 검색
    @GetMapping("/username")
    public ResponseEntity<Response<UserFindResponse>> findUsername(@RequestBody String username) {
        UserFindResponse result = userService.findUsername(username);
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 회원 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Response<UserFindResponse>> findOne(@PathVariable Long id) {
        UserFindResponse result = userService.findOne(id);
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 회원 정보 수정
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        userService.update(request, details.getUser());
        return ResponseEntity.noContent().build();
    }

    // 회원 정보 삭제
    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal PrincipalDetails details) {
        userService.delete(details.getUser());
        return ResponseEntity.noContent().build();
    }
}
