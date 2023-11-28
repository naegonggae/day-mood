package com.final_project_leesanghun_team2.domain.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponse {

	private String accessToken;

	public static TokenResponse form(String accessToken) {
		return new TokenResponse(accessToken);
	}
}