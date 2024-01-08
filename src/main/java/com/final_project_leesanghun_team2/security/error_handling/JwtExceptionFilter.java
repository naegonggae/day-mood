package com.final_project_leesanghun_team2.security.error_handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		logger.info("JwtExceptionFilter 시작");
		try {
			filterChain.doFilter(request, response);
		} catch (RuntimeException exception) {
			logger.error("예외 캐치 : "+ exception.getClass().getSimpleName());
			setErrorResponse(request, response, exception);
		}
		logger.info("JwtExceptionFilter 끝");
	}

	private void setErrorResponse(HttpServletRequest req, HttpServletResponse res,
			RuntimeException e) throws IOException {
		logger.error("setErrorResponse 시작");

		// 여기에서 원하는 에러 응답을 생성하고 응답 코드를 설정합니다.
		String errorName = e.getClass().getSimpleName();
		String errorMessage = e.getMessage();
		logger.error("ErrorResponse: "+e.getClass().getSimpleName()+" - "+e.getMessage());

		// JSON 형식의 오류 응답을 생성합니다.
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", errorName);
		errorResponse.put("message", errorMessage);

		// HTTP 응답 설정
		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);
		res.setCharacterEncoding("UTF-8");

		// JSON 응답 데이터를 HTTP 응답에 쓰기
		objectMapper.writeValue(res.getWriter(), errorResponse);
	}
}
