package com.final_project_leesanghun_team2.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project_leesanghun_team2.exception.ErrorResponse;
import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.exception.ErrorCode;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("인증 실패");
        ObjectMapper objectMapper = new ObjectMapper();
        UserSnsException snsException = new UserSnsException(ErrorCode.INVALID_TOKEN);
        Response<ErrorResponse> error = Response.error(snsException.getErrorCode().getErrorResult());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(error)); //Response객체를 response의 바디값으로 파싱
    }
}
