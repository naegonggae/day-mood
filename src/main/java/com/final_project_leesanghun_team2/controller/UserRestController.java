package com.final_project_leesanghun_team2.controller;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.request.*;
import com.final_project_leesanghun_team2.domain.response.UserJoinResponse;
import com.final_project_leesanghun_team2.domain.response.UserLoginResponse;
import com.final_project_leesanghun_team2.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Api(tags = {"User API"})
public class UserRestController {

    private final UserService userService;

    /** 회원가입 **/
    @ApiOperation(value = "회원 가입", notes = "userName, password로 회원 데이터 저장")
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest){
        return Response.success(userService.join(userJoinRequest));
        // Q: 파라미터 구성을 직관적으로 보이게 짜는게 좋을까? 아니면 객체로 받는게 좋을까?
        // A: 객체로 받고 추가할거 하면될듯.
    }

    /** 로그인 **/
    @ApiOperation(value = "회원 로그인", notes = "userName, password로 저장된 회원 데이터가 있으면 jwt Token 반환")
    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        return Response.success(userService.login(userLoginRequest));
    }
}
