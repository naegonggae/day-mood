package com.final_project_leesanghun_team2.exception.user;

import com.final_project_leesanghun_team2.exception.DayMoodException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchUserException extends DayMoodException {

	private static final String MESSAGE = "존재하지 않는 사용자입니다.";
	private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

	public NoSuchUserException() {
		super(MESSAGE, STATUS);
	}
}
