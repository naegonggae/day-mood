package com.final_project_leesanghun_team2.security.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
	// 인증되지 않은 사용자가 보호된 리소스에 접근할때 동작. 401 Unauthorized
	private final ObjectMapper objectMapper;

	public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {
		log.error("commence 이 실행됨");
		log.error("requestURI : {}", request.getRequestURI());

		// 여기에서 원하는 에러 응답을 생성하고 응답 코드를 설정합니다.
		String errorName = e.getClass().getSimpleName();
		String errorMessage = e.getMessage();

		log.info("ErrorResponse: "+e.getClass().getSimpleName()+" - "+e.getMessage());

//		if (exception.getClass().equals(InsufficientAuthenticationException.class)) {
//			errorName = "InsufficientAuthenticationException";
//			errorMessage = "인증이 필요한 작업입니다.";
//		}
//		else if (exception.getClass().equals(JWTDecodeException.class)) {
//			errorName = "JWTDecodeException";
//			errorMessage = "토큰 구성이 올바르지 않습니다.";
//		}
//		else if (exception.getClass().equals(TokenExpiredException.class)) {
//			errorName = "TokenExpiredException";
//			errorMessage = "토큰 기간이 만료되었습니다. 다시 로그인해주세요!";
//		}
//		else if (exception.getClass().equals(BadCredentialsException.class)) {
//			errorName = "BadCredentialsException";
//			errorMessage = "비밀번호가 틀렸습니다.";
//		}

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
