package com.final_project_leesanghun_team2.domain.dto.post;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequest {

    private String content;
    private boolean visibility;
    private List<String> tagList;

}
