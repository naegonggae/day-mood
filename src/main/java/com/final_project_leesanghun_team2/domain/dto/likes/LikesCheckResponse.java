package com.final_project_leesanghun_team2.domain.dto.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikesCheckResponse {

	private boolean isPush;

	public static LikesCheckResponse from(boolean isPush) {
		return new LikesCheckResponse(isPush);
	}
}
