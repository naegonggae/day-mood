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
	private boolean push;
	private Long totalPost;

	public UserFindResponse(User user, Long totalPost) {
		this.id = user.getId();
		this.nickName = user.getNickName();
		this.followNum = user.getFollowNum();
		this.followingNum = user.getFollowingNum();
		this.totalPost = totalPost;
	}

	// view controller 에서 user 정보를 받아 주입해준다.
	public void setPush(boolean push) {
		this.push = push;
	}
}
