package com.final_project_leesanghun_team2.exception;

import com.final_project_leesanghun_team2.domain.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManger {
/*
    @ExceptionHandler(UserSnsException.class)
    public ResponseEntity<?> userSnsException(UserSnsException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(errorResponse));
    }

 */

    @ExceptionHandler(UserSnsException.class)
    public ResponseEntity<?> snsExceptionHandler(UserSnsException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().getErrorResult()));
    }
}
