package com.final_project_leesanghun_team2.security.jwt;

import com.final_project_leesanghun_team2.domain.dto.user.AuthorizationDto;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.utils.JwtTokenUtil;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		logger.info("인증이나 권한이 필요한 주소가 요청됨");

		// header 에 Authorization 항목 가져오기
		String jwtHeader = request.getHeader(jwtTokenUtil.HEADER_STRING); // "Authorization"
		String requestURI = request.getRequestURI();
		logger.info("requestURI : "+requestURI);

		// 회원가입, 로그인은 인증하지 않음
		if (requestURI.equals("/api/v1/users/join") || requestURI.equals("/api/v1/users/login")) {
			logger.info("회원가입, 로그인은 JwtAuthorizationFilter 에서 인증하지 않는다.");
			chain.doFilter(request, response);
			return;
		}

		//header 가 있는지 확인
		if (jwtHeader == null || !jwtHeader.startsWith(jwtTokenUtil.TOKEN_PREFIX)) {
			logger.info("헤더에 토큰이 없거나 형식이 잘못되었습니다.");
			logger.info("Get 요청은 허용, 그 외의 요청은 인증이 필요하기 때문에 JwtAuthenticationEntryPoint 에서 예외처리 됩니다.");
			chain.doFilter(request, response);
			return;
		}

		logger.info("인증이 필요한 Post, Put, Delete 요청이 수행됩니다.");

		//JWT 토큰을 검증해서 정상적인 사용자인지 확인

		// 헤더 형식 제외하고 생 토큰만 추출
		String jwtToken = request.getHeader(jwtTokenUtil.HEADER_STRING).replace(jwtTokenUtil.TOKEN_PREFIX, "");

		// 서명 검증
		AuthorizationDto authorizationDto = jwtTokenUtil.validateToken(jwtToken);
		log.info("validateToken 통과, {}의 토큰 서명이 정상입니다.", authorizationDto.getUsername());

		String username = authorizationDto.getUsername();
		String newAccessToken = authorizationDto.getAccessToken();

		// 새로운 토큰을 받았다면 쿠키에 등록하기
		if (newAccessToken != null) {
			log.info("새로 갱신한 accessToken 을 쿠키에 등록합니다.");
			Cookie cookie = new Cookie("accessToken", authorizationDto.getAccessToken());
			cookie.setPath("/");
//        cookie.setSecure(true); // https 연결
			cookie.isHttpOnly();
			cookie.setMaxAge(60 * 60 * 24 * 7 * 2); // 쿠키 유효 시간 설정 (초 단위, 2주)
			response.addCookie(cookie);
		}

		// 서명이 정상적으로됨
		if (username != null) {
			User userEntity = userRepository.findByUsername(username)
					.orElseThrow(NoSuchUserException::new);
			log.info("{}님이 존재합니다.", username);

			// jwt 토큰 서명을 통해서 서명이 정상이면 authentication 객체가 만들어준다.
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					principalDetails, null, principalDetails.getAuthorities()); // 비밀번호는 null 처리

			// 강제로 시큐리티 세션에 접근해서 authentication 객체를 저장
			// 세션이 유지 될동안 저장이된다. 저장되어있는 동안에는 인가를 검증할때마다 활용된다.
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}

		logger.info("인증이 필요한 요청 끝");
		chain.doFilter(request, response); // 다음 필터로 이동
	}
}