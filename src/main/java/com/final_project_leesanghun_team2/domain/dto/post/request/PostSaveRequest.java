package com.final_project_leesanghun_team2.domain.dto.post.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveRequest {

    @Size(min = 2, message = "글 내용은 최소 2글자 이상 작성해주세요.")
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    private List<String> tagList;

}