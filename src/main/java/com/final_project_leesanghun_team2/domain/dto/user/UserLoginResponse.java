package com.final_project_leesanghun_team2.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {

	private Long id;

	public static UserLoginResponse from(Long id) {
		return new UserLoginResponse(id);
	}
}
