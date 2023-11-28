package com.final_project_leesanghun_team2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public static Response<ErrorResponse> error(ErrorResponse errorResponse){
        return new Response<>("ERROR", errorResponse);
    }
    public static <T> Response<T> success(T result) {
        return new Response("SUCCESS", result);
    }
}
