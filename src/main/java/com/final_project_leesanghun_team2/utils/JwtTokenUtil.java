package com.final_project_leesanghun_team2.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project_leesanghun_team2.domain.dto.user.AuthorizationDto;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.domain.dto.user.TokenResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {

	@Value("${jwt.token.secret}")
	private String SECRET;
	@Value("${jwt.token.access_expire.length}")
	private int ACCESS_TOKEN_EXPIRATION_TIME;
	@Value("${jwt.token.refresh_expire.length}")
	public int REFRESH_TOKEN_EXPIRE_TIME;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	@Qualifier("refreshTokenRedisTemplate")
	private final RedisTemplate<String, String> refreshTokenRedisTemplate;

	// 토큰 생성
	public TokenResponse createToken(PrincipalDetails principalDetails) {

		Long userId = principalDetails.getUser().getId();

		//access token 생성
		String accessToken = createAccessToken(
				principalDetails.getUser().getId(),
				principalDetails.getUsername(),
				principalDetails.getUser().getRole().toString());

		//refresh token 생성
		String refreshToken = createRefreshToken(
				principalDetails.getUser().getId(),
				principalDetails.getUsername(),
				principalDetails.getUser().getRole().toString());
		log.info("accessToken 과 refreshToken 이 정상적으로 생성되었습니다.");

		return TokenResponse.form(userId, accessToken, refreshToken);
	}

	// 토큰 검증
	public AuthorizationDto validateToken(String accessToken)
			throws AuthenticationException, JsonProcessingException {

		log.info("validateToken 시작");
		try {
			
			// 서명 검증
			String username = JWT.require(Algorithm.HMAC512(SECRET))
					.build().verify(accessToken).getClaim("username").asString();

			log.info("{}의 토큰을 검증했습니다.: ", username);
			return AuthorizationDto.of(username, null);
		} catch (SignatureVerificationException e) {
			log.error("SignatureVerificationException - 토큰의 서명이 올바르지 않습니다.");
			throw new JwtException("토큰의 서명이 올바르지 않습니다. 변조되었을 우려가 있으니 확인해보십시오.", e);
		} catch (MalformedJwtException e) {
			log.error("MalformedJwtException - JWT 형식이 올바르지 않습니다.");
			throw new MalformedJwtException("JWT 형식이 올바르지 않습니다.", e);
		} catch (ExpiredJwtException e) {
			log.error("ExpiredJwtException - 토큰이 만료되었습니다.");
			throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "토큰이 만료되었습니다");
		} catch (UnsupportedJwtException e) {
			log.error("UnsupportedJwtException - 지원하지 않는 JWT 기능이 사용되었습니다.");
			throw new UnsupportedJwtException("지원하지 않는 JWT 기능이 사용되었습니다. 암호화 방식을 확인해 주세요.");
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException - JWT 구문이 잘못되거나 인수가 잘못되었습니다.");
			throw new IllegalArgumentException("JWT 구문이 잘못되거나 인수가 잘못되었습니다.", e);
		} catch (TokenExpiredException e) {
			log.info("TokenExpiredException 발생");
			return refreshAccessToken(accessToken, e);
		}
	}

	private AuthorizationDto refreshAccessToken(String accessToken, TokenExpiredException e)
			throws JsonProcessingException {
		// 토큰 payload 정보 추출
		String[] jwtParts = accessToken.split("\\.");
		String encodedPayload = jwtParts[1];
		String decodedPayload = decodeBase64(encodedPayload);

		// Jackson 을 사용하여 JSON 문자열을 객체로 변환
		ObjectMapper objectMapper = new ObjectMapper();

		// 변환된 객체에서 각 항목에 접근
		JsonNode jsonNode = objectMapper.readTree(decodedPayload);
		Long tokenSub = Long.valueOf(jsonNode.get("sub").asText());
		String tokenUsername = jsonNode.get("username").asText();
		String tokenRole = jsonNode.get("role").asText();

		// Redis 에서 RefreshToken 조회
		String refreshToken = refreshTokenRedisTemplate.opsForValue().get(tokenUsername);
		if (refreshToken != null) {
			
			// 새로운 accessToken 생성
			String newAccessToken = createAccessToken(tokenSub, tokenUsername, tokenRole);

			log.info("{}의 토큰이 만료되었지만 refreshToken 을 통해 갱신했습니다.: ", tokenUsername);
			return AuthorizationDto.of(tokenUsername, newAccessToken);
		} else {
			log.error("refreshToken 이 존재하지 않아 TokenExpiredException 를 발생시킵니다.");
			throw new TokenExpiredException("로그인 유지가능한 기간이 지났습니다. 다시 로그인하여 이용해 주세요", e.getExpiredOn());
		}
	}

	// base64 디코딩
	private static String decodeBase64(String encodedString) {
		byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedString);
		return new String(decodedBytes, StandardCharsets.UTF_8);
	}

	// 엑세스 토큰 생성
	private String createAccessToken(Long userId, String username, String userRole) {
		String accessToken = JWT.create()
				.withSubject(String.valueOf(userId))
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis()+ ACCESS_TOKEN_EXPIRATION_TIME)) // 만료시간
				.withClaim("username", username) // 이메일
				.withClaim("role", userRole)
				.sign(Algorithm.HMAC512(SECRET));
		return accessToken;
	}

	// 리프레쉬 토큰 생성
	private String createRefreshToken(Long userId, String username, String userRole) {
		String refreshToken = JWT.create()
				.withSubject(String.valueOf(userId))
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis()+ REFRESH_TOKEN_EXPIRE_TIME)) // 만료시간
				.withClaim("username", username) // 이메일
				.withClaim("role", userRole)
				.sign(Algorithm.HMAC512(SECRET));
		return refreshToken;
	}

}