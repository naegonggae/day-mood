package com.final_project_leesanghun_team2.exception.post;

import com.final_project_leesanghun_team2.exception.DreamTracksException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchPostException extends DreamTracksException {

	private static final String MESSAGE = "존재하지 않는 게시물입니다.";
	private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

	public NoSuchPostException() {
		super(MESSAGE, STATUS);
	}
}
