package com.final_project_leesanghun_team2.domain.dto.comment.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentSaveRequest {

    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;

}
