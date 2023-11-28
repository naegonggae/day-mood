package com.final_project_leesanghun_team2.exception.user;

import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(DuplicateUsernameException.class)
	public ResponseEntity<?> duplicateUsernameException(DuplicateUsernameException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

	@ExceptionHandler(NoSuchUserException.class)
	public ResponseEntity<?> noSuchUserException(NoSuchUserException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

	@ExceptionHandler(PermissionDeniedException.class)
	public ResponseEntity<?> permissionDeniedException(PermissionDeniedException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

}
