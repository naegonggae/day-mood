package com.final_project_leesanghun_team2.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostModifyRequest {
    private String title;
    private String body;
}
