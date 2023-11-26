package com.final_project_leesanghun_team2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public static <T> Response<T> error(T result) {
        return new Response("ERROR", result);
    }

    public static <T> Response<T> success(T result) {
        return new Response("SUCCESS", result);
    }
}
