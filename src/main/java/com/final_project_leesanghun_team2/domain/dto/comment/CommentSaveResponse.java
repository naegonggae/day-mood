package com.final_project_leesanghun_team2.domain.dto.comment;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentSaveResponse {

	private Long id;

	public static CommentSaveResponse from(Comment comment) {
		return new CommentSaveResponse(comment.getId());
	}
}
