package com.final_project_leesanghun_team2.domain.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationDto {

	private String username;
	private String accessToken;

	public static AuthorizationDto of(String username, String accessToken) {
		return new AuthorizationDto(username, accessToken);
	}

}
