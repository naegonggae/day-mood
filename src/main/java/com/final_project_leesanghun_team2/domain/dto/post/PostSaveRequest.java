package com.final_project_leesanghun_team2.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveRequest {

    private String content;
    private boolean visibility;
}
