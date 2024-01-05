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
	private Long followNum;
	private Long followingNum;
	private boolean isFollow;
	private Long totalPost;

	public static UserFindResponse of(User user, Long followNum, Long followingNum, boolean isFollow, Long totalPost) {
		return new UserFindResponse(user.getId(), user.getNickName(), followNum, followingNum, isFollow, totalPost);
	}
}
