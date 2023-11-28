package com.final_project_leesanghun_team2.domain.dto.tag;

import com.final_project_leesanghun_team2.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagSaveResponse {

	private Long id;

	public static TagSaveResponse from(Tag tag) {
		return new TagSaveResponse(tag.getId());
	}

}
