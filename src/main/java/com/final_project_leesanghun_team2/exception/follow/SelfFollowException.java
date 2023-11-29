package com.final_project_leesanghun_team2.exception.follow;

import com.final_project_leesanghun_team2.exception.DreamTracksException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SelfFollowException extends DreamTracksException {

	private final static String MESSAGE = "'나'는 '나'를 팔로우 할 수 없습니다. ";
	private final static HttpStatus STATUS = HttpStatus.BAD_REQUEST;

	public SelfFollowException() {
		super(MESSAGE, STATUS);

	}
}
