package com.final_project_leesanghun_team2.domain.dto.user;

import com.final_project_leesanghun_team2.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

	private Long id;
	private String nickName;

	public static UserInfoResponse from(User user) {
		return new UserInfoResponse(user.getId(), user.getNickName());
	}

}
