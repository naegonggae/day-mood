package com.final_project_leesanghun_team2.security.jwt;

import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.utils.JwtTokenUtil;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// 권한이나 인증이 필요한 특정 주소를 요청했을때 BasicAuthenticationFilter 가 무조건 타게 되어있음
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		System.out.println("JwtAuthorizationFilter : 인증이나 권한이 필요한 주소가 요청됨");

		// header 에 Authorization 항목 가져오기
		String jwtHeader = request.getHeader(jwtTokenUtil.HEADER_STRING); // "Authorization"
		System.out.println("JwtAuthorizationFilter - jwtHeader = " + jwtHeader);

		// join, login 주소로 들어오는 요청인 경우 다음 필터 또는 요청 핸들러로 요청을 전달하지 않고 바로 리턴
		String requestURI = request.getRequestURI();
		System.out.println("주소는 : " + requestURI);

		// 회원가입, 로그인 api 는 뚫어줘야함
		if (requestURI.equals("/api/v1/auth/join") || requestURI.equals("/api/v1/auth/login")) {
			chain.doFilter(request, response);
			return;
		}

		//header 가 있는지 확인
		if (jwtHeader == null || !jwtHeader.startsWith(jwtTokenUtil.TOKEN_PREFIX)) {
			System.out.println("JwtAuthorizationFilter : 헤더가 없거나 형식이 잘못됨");
//			System.out.println(requestURI);

//			throw new JwtException("JWT 형식이 올바르지 않습니다.");
			chain.doFilter(request, response);
			return;
		}

		//JWT 토큰을 검증해서 정상적인 사용자인지 확인

		// 헤더 형식 제외하고 생 토큰만 추출
		String jwtToken = request.getHeader(jwtTokenUtil.HEADER_STRING).replace(jwtTokenUtil.TOKEN_PREFIX, "");
		System.out.println("생 jwtToken = " + jwtToken);


		// 서명 검증
		String username = jwtTokenUtil.validateToken(jwtToken);
		System.out.println("validateToken 토큰 서명 정상");

		// 서명이 정상적으로됨
		if (username != null) {
			System.out.println("JwtAuthorizationFilter username 정상");
			User userEntity = userRepository.findByUsername(username)
					.orElseThrow(UserSnsException::new);
			System.out.println("userEntity = " + userEntity);

			// jwt 토큰 서명을 통해서 서명이 정상이면 authentication 객체가 만들어준다.
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					principalDetails, null, principalDetails.getAuthorities()); // 비밀번호는 null 처리

			// 강제로 시큐리티 세션에 접근해서 authentication 객체를 저장
			// 세션이 유지 될동안 저장이된다. 저장되어있는 동안에는 인가를 검증할때마다 활용된다.
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}

		chain.doFilter(request, response); // 다음 필터로 이동
	}
}