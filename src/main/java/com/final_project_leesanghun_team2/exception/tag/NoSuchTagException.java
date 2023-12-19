package com.final_project_leesanghun_team2.exception.tag;

import com.final_project_leesanghun_team2.exception.DayMoodException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchTagException extends DayMoodException {

	private static final String MESSAGE = "존재하지 않는 태그입니다.";
	private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

	public NoSuchTagException() {
		super(MESSAGE, STATUS);
	}
}
