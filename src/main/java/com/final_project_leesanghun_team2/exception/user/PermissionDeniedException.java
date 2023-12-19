package com.final_project_leesanghun_team2.exception.user;

import com.final_project_leesanghun_team2.exception.DayMoodException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PermissionDeniedException extends DayMoodException {

	private static final String MESSAGE = "권한이 없습니다.";
	private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

	public PermissionDeniedException() {
		super(MESSAGE, STATUS);
	}
}
