package com.final_project_leesanghun_team2.domain.dto.post;

import com.final_project_leesanghun_team2.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFindResponse {

    private Long id;

    public static PostFindResponse from(Post post) {
        return new PostFindResponse(post.getId());
    }

}
