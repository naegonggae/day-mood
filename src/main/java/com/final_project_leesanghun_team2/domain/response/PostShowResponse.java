package com.final_project_leesanghun_team2.domain.response;

import com.final_project_leesanghun_team2.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostShowResponse {
    private Integer id;
    private String title;
    private String body;
    private String userName;
    private LocalDateTime createdAt;

    // 포스트 1개 조회할때 Post -> PostShowResponse 로 포장
    public static PostShowResponse of(Post post) {
        return new PostShowResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getUser().getUserName(),
                post.getCreatedAt()
        );
    }

    // 포스트 리스트 조회할때 Page<Entity> -> Page<Dto> 로 포장
    public static Page<PostShowResponse> toList(Page<Post> post){
        Page<PostShowResponse> postDtoList = post.map(m -> PostShowResponse.builder() // 이게 뭐야...
                .id(m.getId())
                .title(m.getTitle())
                .body(m.getBody())
                .userName(m.getUser().getUserName())
                .createdAt(m.getCreatedAt())
                .build());
        return postDtoList;
    }
}
