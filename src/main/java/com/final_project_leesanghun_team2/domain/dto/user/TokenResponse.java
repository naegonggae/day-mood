package com.final_project_leesanghun_team2.domain.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponse {

	private Long id;
	private String accessToken;
	private String refreshToken;

	public static TokenResponse form(Long id, String accessToken, String refreshToken) {
		return new TokenResponse(id, accessToken, refreshToken);
	}
}