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
	private boolean push; // 좋아요 눌렀는지
	private boolean me; // 팔로잉유저와 내가 같은지


	public static FollowPeopleResponse following(Follow follow, boolean isPush, boolean me) {

		return new FollowPeopleResponse(
				follow.getFollowingUser().getId(),
				follow.getFollowingUser().getNickName(),
				isPush,
				me);
	}

	public static FollowPeopleResponse follow(Follow follow, boolean isPush, boolean me) {

		return new FollowPeopleResponse(
				follow.getFollowUser().getId(),
				follow.getFollowUser().getNickName(),
				isPush,
				me);
	}
}
