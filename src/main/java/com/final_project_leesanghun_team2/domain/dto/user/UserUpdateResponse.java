package com.final_project_leesanghun_team2.domain.dto.user;

import com.final_project_leesanghun_team2.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateResponse {

	private Long id;

	public static UserUpdateResponse from(User user) {
		return new UserUpdateResponse(user.getId());
	}

}
