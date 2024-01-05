package com.final_project_leesanghun_team2.domain.dto.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikesPushResponse {

	private boolean isLike;
	private Long likeCount;

	public static LikesPushResponse of(boolean isLike, Long likeCount) {
		return new LikesPushResponse(isLike, likeCount);
	}
}
