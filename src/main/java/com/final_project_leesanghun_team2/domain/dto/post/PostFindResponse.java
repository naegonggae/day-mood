package com.final_project_leesanghun_team2.domain.dto.post;

import static java.util.stream.Collectors.toList;

import com.final_project_leesanghun_team2.domain.entity.Post;
import com.final_project_leesanghun_team2.domain.entity.TagPost;
import com.final_project_leesanghun_team2.domain.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFindResponse {

    private Long id;
    private String content;
    private List<TagPostDto> tagList;
    private Long userId;
    private String userNickName;
    private boolean isLike; // 로그인한 유저가 게시물에 좋아요를 눌렀는지 여부
    private int likeCount;

    public PostFindResponse(Post post, User loginUser) {
        this.id = post.getId();
        this.content = post.getContent();
        this.tagList = post.getTagPostList().stream() // TagPost 초기화
                .map(t -> new TagPostDto(t))
                .collect(toList());
        this.userId = post.getUser().getId(); // User 초기화
        this.userNickName = post.getUser().getNickName();
        this.isLike = post.getLikeList().stream() // Likes 초기화
                .anyMatch(like -> like.getUser().getId().equals(loginUser.getId()));
        this.likeCount = post.getLikeList().size();
    }

    @Getter
    static class TagPostDto {
        private String name;
        public TagPostDto(TagPost tagPost) {
            this.name = tagPost.getTag().getName(); // Tag 초기화
        }
    }

}