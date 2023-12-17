package com.final_project_leesanghun_team2.domain.dto.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikesPushResponse {

	private boolean isPush;
	private Long count;

	public static LikesPushResponse from(boolean status, Long count) {
		return new LikesPushResponse(status, count);
	}
}
