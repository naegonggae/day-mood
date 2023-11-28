package com.final_project_leesanghun_team2.exception.tag;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TagExceptionHandler {

	@ExceptionHandler(NoSuchTagException.class)
	public ResponseEntity<?> noSuchTagException(NoSuchTagException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

}
