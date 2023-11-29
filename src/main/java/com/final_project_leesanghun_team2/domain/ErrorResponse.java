package com.final_project_leesanghun_team2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    public String exceptionName;
    public String message;

    public static ErrorResponse from(Exception e) {
        return new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
    }
}