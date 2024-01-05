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
	private Long followCount;
	private Long followingCount;
	private boolean isFollow;
	private Long postCount;

	public static UserFindResponse of(User user, Long followCount, Long followingCount, boolean isFollow, Long postCount) {
		return new UserFindResponse(user.getId(), user.getNickName(), followCount, followingCount, isFollow, postCount);
	}
}
