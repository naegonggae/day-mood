package com.final_project_leesanghun_team2.security.error_handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {
		log.error("인증되지 않은 사용자가 보호된 리소스에 접근할때 동작, 401 Unauthorized");
		log.error("requestURI : {}", request.getRequestURI());

		// 여기에서 원하는 에러 응답을 생성하고 응답 코드를 설정합니다.
		String errorName = e.getClass().getSimpleName();
		String errorMessage = e.getMessage();
		log.error("ErrorResponse: "+e.getClass().getSimpleName()+" - "+e.getMessage());

		// JSON 형식의 오류 응답을 생성합니다.
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", errorName);
		errorResponse.put("message", errorMessage);

		// HTTP 응답 설정
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		// JSON 응답 데이터를 HTTP 응답에 쓰기
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}
