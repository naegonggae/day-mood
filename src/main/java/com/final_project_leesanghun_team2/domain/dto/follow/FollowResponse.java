package com.final_project_leesanghun_team2.domain.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponse {

	private boolean isFollow;
	private Long followCount;
	private Long followingCount;

	public static FollowResponse of(boolean isFollow, Long followCount, Long followingCount) {
		return new FollowResponse(isFollow, followCount, followingCount);
	}
}
