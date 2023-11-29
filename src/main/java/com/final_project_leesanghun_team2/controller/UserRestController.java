package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.dto.user.UserUpdateResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UsernameCondition;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Api(tags = {"User API"})
@Slf4j
public class UserRestController {

    private final UserService userService;

    // 회원가입
    @ApiOperation(value = "회원 가입", notes = "userName, password로 회원 데이터 저장")
    @PostMapping("/join")
    public ResponseEntity<Response<UserJoinResponse>> join(@Valid @RequestBody UserJoinRequest request) {
        log.info("join 시작");
        UserJoinResponse result = userService.join(request);
        log.info("join 끝");
        return ResponseEntity.created(URI.create("/api/v1/users/join"+result.getId()))
                .body(Response.success(result));
    }

    // 로그인
    @ApiOperation(value = "회원 로그인", notes = "userName, password로 저장된 회원 데이터가 있으면 jwt Token 반환")
    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@RequestBody UserLoginRequest request,
            HttpServletResponse response) {
        log.info("login 시작");
        TokenResponse result = userService.login(request);

        // access Token 은 body 로 전송
        response.addHeader("Authorization", "Bearer " + result.getAccessToken());

        log.info("login 끝");
        return ResponseEntity.ok().body(Response.success("로그인 되었습니다."));
    }

    // 회원이름으로 검색
    @GetMapping("/nickname")
    public ResponseEntity<Response<UserFindResponse>> findUsername(@RequestBody UsernameCondition condition) {
        log.info("findUsername 시작");
        UserFindResponse result = userService.findUsername(condition.getNickName());
        log.info("findUsername 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 회원 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Response<UserFindResponse>> findOne(@PathVariable Long id) {
        log.info("findOne 시작");
        UserFindResponse result = userService.findOne(id);
        log.info("findOne 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 회원 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Response<UserUpdateResponse>> update(@PathVariable Long id,
            @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("update 시작");
        UserUpdateResponse result = userService.update(id, request, details.getUser());
        log.info("update 끝");
        return ResponseEntity.ok().body(Response.success(result));
    }

    // 회원 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails details) {
        log.info("delete 시작");
        userService.delete(id, details.getUser());
        log.info("delete 끝");
        return ResponseEntity.noContent().build();
    }
}
