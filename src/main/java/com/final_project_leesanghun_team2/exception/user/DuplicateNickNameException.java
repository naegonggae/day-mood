package com.final_project_leesanghun_team2.exception.user;

import com.final_project_leesanghun_team2.exception.DayMoodException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateNickNameException extends DayMoodException {

	private static final String MESSAGE = "사용중인 닉네임입니다.";
	private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

	public DuplicateNickNameException() {
		super(MESSAGE, STATUS);
	}
}
