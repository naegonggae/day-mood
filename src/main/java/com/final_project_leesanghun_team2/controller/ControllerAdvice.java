package com.final_project_leesanghun_team2.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.exception.DayMoodException;
import com.final_project_leesanghun_team2.domain.ErrorResponse;
import io.jsonwebtoken.JwtException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

	private final HttpStatus BR = HttpStatus.BAD_REQUEST;
	private final HttpStatus ISE = HttpStatus.INTERNAL_SERVER_ERROR;
	private final HttpStatus UA = HttpStatus.UNAUTHORIZED;

	@ExceptionHandler(DayMoodException.class)
	public ResponseEntity<Response> dayMoodExceptionHandler(DayMoodException exception) {
		log.error("에러 발생 {} : {}", exception.getClass().getSimpleName(), exception.getMessage());
		return ResponseEntity.status(exception.getStatus())
				.body(Response.error(ErrorResponse.from(exception)));
	}

	// Valid 예외 핸들링
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response> invalidArgumentHandler(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		StringBuilder builder = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append("[");
			builder.append(fieldError.getField());
			builder.append("](은)는 ");
			builder.append(fieldError.getDefaultMessage());
			builder.append(" 입력된 값: [");
			builder.append(fieldError.getRejectedValue());
			builder.append("]");
		}
		log.error("에러 발생 {} : {}", exception.getClass().getSimpleName(), exception.getMessage());
		return ResponseEntity.status(BR)
				.body(Response.error(new ErrorResponse(exception.getClass().getSimpleName(), builder.toString())));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Response> invalidParamHandler(ConstraintViolationException exception) {
		log.error("에러 발생 {} : {}", exception.getClass().getSimpleName(), exception.getMessage());
		return ResponseEntity.status(BR)
				.body(Response.error(new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage())));
	}

	@ExceptionHandler({InvalidFormatException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
	public ResponseEntity<Response> invalidFormatHandler(Exception exception) {
		log.error("에러 발생 {} : {}", exception.getClass().getSimpleName(), exception.getMessage());
		return ResponseEntity.status(BR)
				.body(Response.error(new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage())));
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Response> invalidDataAccessHandler(DataAccessException exception) {
		log.error("에러 발생 {} : {}", exception.getClass().getSimpleName(), exception.getMessage());
		return ResponseEntity.status(ISE)
				.body(Response.error(new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage())));
	}

	@ExceptionHandler(JwtException.class)
	public ResponseEntity<Response> jwtExceptionHandler(JwtException exception) {
		log.error("에러 발생 {} : {}", exception.getClass().getSimpleName(), exception.getMessage());
		return ResponseEntity.status(UA)
				.body(Response.error(new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage())));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> unhandledExceptionHandler(Exception exception) {
		log.error("에러 발생 {} : {}", exception.getClass().getSimpleName(), exception.getMessage());
		return ResponseEntity.status(ISE)
				.body(Response.error(new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage())));
	}


}
