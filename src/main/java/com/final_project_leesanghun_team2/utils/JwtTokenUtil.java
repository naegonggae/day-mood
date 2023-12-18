package com.final_project_leesanghun_team2.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.domain.dto.user.TokenResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {

	@Value("${jwt.token.secret}")
	private String SECRET;
	@Value("${jwt.token.expire.length}")
	private int EXPIRATION_TIME; // 10일 (1/1000초) // (60000 * 10) -> 60000 =1분
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";

	public TokenResponse createToken(PrincipalDetails principalDetails) {

		Long userId = principalDetails.getUser().getId();
		//access token 생성
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME)) // 만료시간
				.withClaim("username", principalDetails.getUsername())
				.withClaim("role", principalDetails.getUser().getRole().toString())
				.sign(Algorithm.HMAC512(SECRET)); // SECRET = 사이트만 알고있는 고유값

		return TokenResponse.form(userId, jwtToken);
	}

	public String validateToken(String jwtToken) throws AuthenticationException{

		log.info("validateToken 시작");
		try {
			String username = JWT.require(Algorithm.HMAC512(SECRET))
					.build().verify(jwtToken).getClaim("username").asString();
			log.info("username: "+username);
			return username;
		} catch (SignatureVerificationException e) {
			throw new JwtException("토큰의 서명이 올바르지 않습니다. 변조되었을 우려가 있으니 확인해보십시오.", e);
		} catch (MalformedJwtException e) {
			throw new MalformedJwtException("JWT 형식이 올바르지 않습니다.", e);
		} catch (ExpiredJwtException e) {
			throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "토큰이 만료되었습니다");
		} catch (UnsupportedJwtException e) {
			throw new UnsupportedJwtException("지원하지 않는 JWT 기능이 사용되었습니다. 암호화 방식을 확인해 주세요.");
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("JWT 구문이 잘못되거나 인수가 잘못되었습니다.", e);
		} catch (TokenExpiredException e) {
			throw new TokenExpiredException("토큰의 유효 기간이 만료되었습니다.", e.getExpiredOn());
		} catch (Exception e) {
		}
		return "토큰 인증 과정에서 알 수 없는 오류가 발생했습니다";
	}

}
