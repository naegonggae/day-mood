package com.final_project_leesanghun_team2.domain.dto.user;

import com.final_project_leesanghun_team2.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFindResponse {

	private Long id;
	private String nickName;

	public static UserFindResponse from(User user) {
		return new UserFindResponse(user.getId(), user.getNickName());
	}
}
