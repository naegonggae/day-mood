package com.final_project_leesanghun_team2.domain.dto.post;

import com.final_project_leesanghun_team2.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveResponse {
    private Long id;

    public static PostSaveResponse from(Post post) {
        return new PostSaveResponse(post.getId());
    }

}
