package com.final_project_leesanghun_team2.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DreamTracksException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = "일시적으로 접속이 원활하지 않습니다. tkdtkd975@gmail.com에 문의 부탁드립니다.";
	private final HttpStatus status;

	public DreamTracksException() {
		super(DEFAULT_MESSAGE);
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public DreamTracksException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
}
