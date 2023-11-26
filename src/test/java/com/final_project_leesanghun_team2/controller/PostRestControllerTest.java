package com.final_project_leesanghun_team2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project_leesanghun_team2.configuration.SecurityConfiguration;
import com.final_project_leesanghun_team2.domain.request.PostAddRequest;
import com.final_project_leesanghun_team2.domain.request.PostModifyRequest;
import com.final_project_leesanghun_team2.domain.response.PostResultResponse;
import com.final_project_leesanghun_team2.domain.response.PostShowResponse;
import com.final_project_leesanghun_team2.exception.ErrorCode;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import com.final_project_leesanghun_team2.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostRestController.class)
class PostRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    SecurityConfiguration securityConfiguration;

    @Autowired
    ObjectMapper objectMapper;
/*
    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 조회 성공")
    void show_post_success() throws Exception {

        PostShowResponse postShowResponse = new PostShowResponse(1, "제목", "내용", "이름", null);

        when(postService.showAll(any()))
                .thenReturn();

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.userName").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.body").exists());

    }
 */


    @Test // 이게 뭐야...
    @WithMockUser // 인증된 상태
    @DisplayName("pageable 파라미터 검증")
    void evaluates_pageable_parameter() throws Exception {

        mockMvc.perform(get("/api/v1/posts")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).showAll(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(0, pageable.getPageNumber());
        assertEquals(3, pageable.getPageSize());
        assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());
    }

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 등록 성공")
    void add_post_success() throws Exception {

        PostAddRequest postRequest = new PostAddRequest("제목", "내용");
        PostResultResponse postResultResponse = new PostResultResponse("메세지", 1);

        when(postService.add(any(), any()))
                .thenReturn(postResultResponse);

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @WithAnonymousUser // 인증 된지 않은 상태
    @DisplayName("포스트 등록 실패(1) : 인증 실패")
    void add_post_fail1() throws Exception {

        PostAddRequest postRequest = new PostAddRequest("제목", "내용");
        PostResultResponse postResultResponse = new PostResultResponse("메세지", 1);

        when(postService.add(any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    //포스트 작성 실패(1) - 인증 실패 - JWT를 Bearer Token으로 보내지 않은 경우
    //포스트 작성 실패(2) - 인증 실패 - JWT가 유효하지 않은 경우
    //어떻게 하나 이건

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 수정 성공")
    void modify_post_success() throws Exception {

        PostModifyRequest postModifyRequest = new PostModifyRequest("제목", "내용");
        PostResultResponse postResultResponse = new PostResultResponse("메세지", 1);

        when(postService.modify(any(), any(), any()))
                .thenReturn(postResultResponse);

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postModifyRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @WithAnonymousUser // 인증 되지 않은 상태
    @DisplayName("포스트 수정 실패(1) : 인증 실패")
    void modify_post_fail1() throws Exception {

        PostModifyRequest postModifyRequest = new PostModifyRequest("제목", "내용");

        when(postService.modify(any(), any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postModifyRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 수정 실패(2) : 포스트 내용 불일치")
    void modify_post_fail2() throws Exception {

        PostModifyRequest postModifyRequest = new PostModifyRequest("제목", "내용");

        when(postService.modify(any(), any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postModifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 수정 실패(3) : 작성자 불일치")
    void modify_post_fail3() throws Exception {

        PostModifyRequest postModifyRequest = new PostModifyRequest("제목", "내용");

        when(postService.modify(any(), any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postModifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 수정 실패(4) : 데이터베이스 에러")
    void modify_post_fail4() throws Exception {

        PostModifyRequest postModifyRequest = new PostModifyRequest("제목", "내용");

        when(postService.modify(any(), any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.DATABASE_ERROR));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postModifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 삭제 성공")
    void delete_post_success() throws Exception {

        PostResultResponse postResultResponse = new PostResultResponse("메세지", 1);

        when(postService.delete(any(), any()))
                .thenReturn(postResultResponse);

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @WithAnonymousUser // 인증 된지 않은 상태
    @DisplayName("포스트 삭제 실패(1) : 인증 실패")
    void delete_post_fail1() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 삭제 실패(2) : 포스트 내용 불일치")
    void delete_post_fail2() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 삭제 실패(3) : 작성자 불일치")
    void delete_post_fail3() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser // 인증된 상태
    @DisplayName("포스트 삭제 실패(4) : 데이터베이스 에러")
    void delete_post_fail4() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new UserSnsException(ErrorCode.DATABASE_ERROR));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }
}