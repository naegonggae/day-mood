package com.final_project_leesanghun_team2.domain.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagFindResponse {

	private boolean isExist;

	public static TagFindResponse from(boolean isExist) {
		return new TagFindResponse(isExist);
	}
}
