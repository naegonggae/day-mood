package com.final_project_leesanghun_team2.domain.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagFindResponse {

	private boolean isTagExist;

	public static TagFindResponse from(boolean isTagExist) {
		return new TagFindResponse(isTagExist);
	}
}
