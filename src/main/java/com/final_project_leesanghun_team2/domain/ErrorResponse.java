package com.final_project_leesanghun_team2.domain;

import com.final_project_leesanghun_team2.exception.DreamTracksException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    public HttpStatus httpStatus;
    public int stateType;
    public String message;

    public static ErrorResponse from(DreamTracksException e) {
        return new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage());
    }
}