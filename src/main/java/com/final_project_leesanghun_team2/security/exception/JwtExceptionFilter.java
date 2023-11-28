package com.final_project_leesanghun_team2.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
		System.out.println("JwtExceptionFilter 시작");
		try {
			filterChain.doFilter(request, response);
		} catch (JwtException exception) {
			System.out.println("JwtExceptionFilter 에서 에러 캐치");
			setErrorResponse(request, response, exception);
		}
		System.out.println("JwtExceptionFilter 끝");

	}

	private void setErrorResponse(HttpServletRequest req, HttpServletResponse res,
			JwtException e) throws IOException {

		System.out.println("JwtExceptionFilter - setErrorResponse 시작");

		// 여기에서 원하는 에러 응답을 생성하고 응답 코드를 설정합니다.
		String errorName = "JwtException";
		String errorMessage = "JWT 형식이 올바르지 않거나 JWT 관련 알 수 없는 오류가 발생했습니다.";

		System.out.println("errorName : " + e.toString());

		String error = e.getCause().getClass().getSimpleName();
		if (error.equals("SignatureVerificationException")) {
			errorName = "SignatureVerificationException";
			errorMessage = "토큰의 서명이 올바르지 않습니다. 변조되었을 우려가 있으니 확인해보십시오.";
		}
		else if (error.equals("MalformedJwtException")) {
			errorName = "MalformedJwtException";
			errorMessage = "JWT 형식이 올바르지 않습니다.";
		}
		else if (error.equals("ExpiredJwtException")) {
			errorName = "ExpiredJwtException";
			errorMessage = "토큰이 만료되었습니다.";
		}
		else if (error.equals("UnsupportedJwtException")) {
			errorName = "UnsupportedJwtException";
			errorMessage = "지원하지 않는 JWT 기능이 사용되었습니다. 암호화 방식을 확인해 주세요.";
		}
		else if (error.equals("IllegalArgumentException")) {
			errorName = "IllegalArgumentException";
			errorMessage = "JWT 구문이 잘못되거나 인수가 잘못되었습니다.";
		}
		else if (error.equals("TokenExpiredException")) {
			errorName = "TokenExpiredException";
			errorMessage = "토큰 기간이 만료되었습니다. 다시 로그인해주세요!";
		}
		else if (error.equals("JWTDecodeException")) {
			errorName = "JWTDecodeException";
			errorMessage = "토큰 구성이 올바르지 않습니다.";
		}

		// JSON 형식의 오류 응답을 생성합니다.
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", errorName);
		errorResponse.put("message", errorMessage);
//		errorResponse.put("stateType", "401");
//		errorResponse.put("status", "UNAUTHORIZED");

		// HTTP 응답 설정
		res.setStatus(HttpStatus.UNAUTHORIZED.value());
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		// JSON 응답 데이터를 HTTP 응답에 쓰기
		objectMapper.writeValue(res.getWriter(), errorResponse);
	}
}
