package com.final_project_leesanghun_team2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSnsException extends RuntimeException {

    private ErrorCode errorCode;
    //private String message;
/*
    @Override
    public String toString() {
        if (message == null) return errorCode.getMessage();
        return String.format("%s, %s", errorCode.getMessage(), message);
    }

 */
}
