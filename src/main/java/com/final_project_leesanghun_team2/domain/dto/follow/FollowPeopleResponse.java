package com.final_project_leesanghun_team2.domain.dto.follow;

import com.final_project_leesanghun_team2.domain.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowPeopleResponse {

	private Long id;
	private String nickName;
	private boolean isFollow; // 로그인한 유저가 좋아요를 눌렀는지 여부
	private boolean isSameUser; // 팔로잉 유저와 로그인한 유저가 같은지 여부


	// 팔로잉 목록
	public static FollowPeopleResponse following(Follow follow, boolean isFollow, boolean isSameUser) {
		return new FollowPeopleResponse(
				follow.getFollowingUser().getId(),
				follow.getFollowingUser().getNickName(),
				isFollow,
				isSameUser);
	}

	// 팔로우 목록
	public static FollowPeopleResponse follow(Follow follow, boolean isFollow, boolean isSameUser) {
		return new FollowPeopleResponse(
				follow.getFollowUser().getId(),
				follow.getFollowUser().getNickName(),
				isFollow,
				isSameUser);
	}
}