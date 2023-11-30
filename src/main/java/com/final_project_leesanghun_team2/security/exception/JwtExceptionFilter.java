package com.final_project_leesanghun_team2.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	public JwtExceptionFilter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		logger.info("JwtExceptionFilter 시작");
		try {
			filterChain.doFilter(request, response);
		} catch (RuntimeException exception) {
			logger.info("예외 캐치 : "+ exception.getClass().getSimpleName());
			setErrorResponse(request, response, exception);
		}
		logger.info("JwtExceptionFilter 끝");
	}

	private void setErrorResponse(HttpServletRequest req, HttpServletResponse res,
			RuntimeException e) throws IOException {

		logger.info("setErrorResponse 시작");

		// 여기에서 원하는 에러 응답을 생성하고 응답 코드를 설정합니다.
		String errorName = e.getClass().getSimpleName();
		String errorMessage = e.getMessage();

		logger.info("ErrorResponse: "+e.getClass().getSimpleName()+" - "+e.getMessage());

//		String error = e.getClass().toString();
//		logger.info("에러이름 : "+e.getClass().toString());
//		if (e instanceof SignatureVerificationException) {
//			errorName = "SignatureVerificationException";
//			errorMessage = "토큰의 서명이 올바르지 않습니다. 변조되었을 우려가 있으니 확인해보십시오.";
//		}
//		else if (e instanceof MalformedJwtException) {
//			errorName = "MalformedJwtException";
//			errorMessage = "JWT가 존재하지 않거나 형식이 올바르지 않습니다.";
//		}
//		else if (e instanceof ExpiredJwtException) {
//			errorName = "ExpiredJwtException";
//			errorMessage = "토큰이 만료되었습니다.";
//		}
//		else if (e instanceof UnsupportedJwtException) {
//			errorName = "UnsupportedJwtException";
//			errorMessage = "지원하지 않는 JWT 기능이 사용되었습니다. 암호화 방식을 확인해 주세요.";
//		}
//		else if (e instanceof IllegalArgumentException) {
//			errorName = "IllegalArgumentException";
//			errorMessage = "JWT 구문이 잘못되거나 인수가 잘못되었습니다.";
//		}
//		else if (e instanceof TokenExpiredException) {
//			errorName = "TokenExpiredException";
//			errorMessage = "토큰 기간이 만료되었습니다. 다시 로그인해주세요!";
//		}
//		else if (e instanceof JWTDecodeException) {
//			errorName = "JWTDecodeException";
//			errorMessage = "토큰 구성이 올바르지 않습니다.";
//		}
//		else if (e instanceof NullPointerException) {
//			errorName = "NullPointerException";
//			errorMessage = "헤더가 존재하지 않습니다.";
//		}

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
