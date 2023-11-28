package com.final_project_leesanghun_team2.exception.comment;

import com.final_project_leesanghun_team2.exception.DreamTracksException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchCommentException extends DreamTracksException {

	private static final String MESSAGE = "존재하지 않는 댓글입니다.";
	private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

	public NoSuchCommentException() {
		super(MESSAGE, STATUS);
	}
}
