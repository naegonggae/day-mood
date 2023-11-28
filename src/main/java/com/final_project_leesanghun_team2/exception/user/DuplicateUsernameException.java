package com.final_project_leesanghun_team2.exception.user;

import com.final_project_leesanghun_team2.exception.DreamTracksException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateUsernameException extends DreamTracksException {

	private static final String MESSAGE = "사용중인 닉네임입니다.";
	private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

	public DuplicateUsernameException() {
		super(MESSAGE, STATUS);
	}
}
