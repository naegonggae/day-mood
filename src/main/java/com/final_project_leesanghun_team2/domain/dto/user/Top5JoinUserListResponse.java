package com.final_project_leesanghun_team2.domain.dto.user;

import com.final_project_leesanghun_team2.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Top5JoinUserListResponse {

	private Long id;
	private String nickName;
	private int index;

	public static Top5JoinUserListResponse of(User user, int num) {
		return new Top5JoinUserListResponse(user.getId(), user.getNickName(), num+1);
	}

}
