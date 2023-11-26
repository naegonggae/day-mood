package com.final_project_leesanghun_team2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project_leesanghun_team2.domain.request.UserJoinRequest;
import com.final_project_leesanghun_team2.domain.response.UserJoinResponse;
import com.final_project_leesanghun_team2.domain.response.UserLoginResponse;
import com.final_project_leesanghun_team2.exception.ErrorCode;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import com.final_project_leesanghun_team2.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@WithMockUser
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void join_success() throws Exception {

        UserJoinRequest userJoinRequest = new UserJoinRequest("sangHun", "1234");
        UserJoinResponse userJoinResponse = new UserJoinResponse(1,"sangHun");

        when(userService.join(any())).thenReturn(userJoinResponse); // 서비스를 실행시켰을때 결과값을 정해준다. 서비스 테스트가아니니까
        // 여기도 실패이면 thenThrow 해줘야함

        mockMvc.perform(post("/api/v1/users/join")
                    .with(csrf()) // security test 추가 후 넣어줘야함
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(userJoinRequest))) // userJoinRequest를 json형태로 변경해시켜서 위의 url로 보내줌
                .andDo(print())
                .andExpect(status().isOk()) // 성공이면 isOk, 아니면 에러 출력
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.userId").exists())
                .andExpect(jsonPath("$.result.userName").exists());
    }

    @Test
    @DisplayName("회원가입 실패 - userName 중복")
    void join_fail() throws Exception{

        UserJoinRequest userJoinRequest = new UserJoinRequest("sangHun", "1234");

        when(userService.join(any())).thenThrow(new UserSnsException(ErrorCode.DUPLICATED_USER_NAME));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {

        UserJoinRequest userJoinRequest = new UserJoinRequest("sangHun", "1234");
        UserLoginResponse userLoginResponse = new UserLoginResponse("token");

        when(userService.login(any())).thenReturn(userLoginResponse);

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.jwt").exists());
    }

    @Test
    @DisplayName("로그인 실패 - id 없음")
    void login_fail1() throws Exception {

        UserJoinRequest userJoinRequest = new UserJoinRequest("sangHun", "1234");

        when(userService.login(any())).thenThrow(new UserSnsException(ErrorCode.USERNAME_NOT_FOUND));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - password 틀림")
    void login_fail2() throws Exception {

        UserJoinRequest userJoinRequest = new UserJoinRequest("sangHun", "1234");

        when(userService.login(any())).thenThrow(new UserSnsException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}