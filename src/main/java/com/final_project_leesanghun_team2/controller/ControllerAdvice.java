package com.final_project_leesanghun_team2.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.exception.DreamTracksException;
import com.final_project_leesanghun_team2.domain.ErrorResponse;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ControllerAdvice {

	private final HttpStatus BR = HttpStatus.BAD_REQUEST;
	private final HttpStatus ISE = HttpStatus.INTERNAL_SERVER_ERROR;

	@ExceptionHandler(DreamTracksException.class)
	public ResponseEntity<Response> dreamTracksExceptionHandler(DreamTracksException exception) {
		return ResponseEntity.status(exception.getStatus())
				.body(Response.error(ErrorResponse.from(exception)));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response> invalidArgumentHandler(MethodArgumentNotValidException exception) {
		return ResponseEntity.status(BR)
				.body(Response.error(new ErrorResponse(BR, BR.value(), exception.getMessage())));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Response> invalidParamHandler(ConstraintViolationException exception) {
		return ResponseEntity.status(BR)
				.body(Response.error(new ErrorResponse(BR, BR.value(), exception.getMessage())));
	}

	@ExceptionHandler({InvalidFormatException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
	public ResponseEntity<Response> invalidFormatHandler(Exception exception) {
		return ResponseEntity.status(BR)
				.body(Response.error(new ErrorResponse(BR, BR.value(), exception.getMessage())));
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Response> invalidDataAccessHandler(DataAccessException exception) {
		return ResponseEntity.status(ISE)
				.body(Response.error(new ErrorResponse(ISE, ISE.value(), exception.getMessage())));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> unhandledExceptionHandler(Exception exception) {
		return ResponseEntity.status(ISE)
				.body(Response.error(new ErrorResponse(ISE, ISE.value(), exception.getMessage())));
	}


}
