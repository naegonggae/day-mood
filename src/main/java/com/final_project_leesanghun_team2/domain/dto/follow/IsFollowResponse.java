package com.final_project_leesanghun_team2.domain.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IsFollowResponse {

	private boolean push;

	public static IsFollowResponse from(boolean isFollow) {
		return new IsFollowResponse(isFollow);
	}

}
