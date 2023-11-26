package com.final_project_leesanghun_team2.domain.response;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentShowResponse {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private LocalDateTime createdAt;

    // 댓글 등록할때 Comment -> CommentShowResponse 로 변경
    public static CommentShowResponse of(Comment comment){
        return new CommentShowResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUser().getUserName(),
                comment.getPost().getId(),
                comment.getCreatedAt()
        );
    }

    // 댓글 조회할때 Page<Comment> -> Page<CommentShowResponse> 로 변경
    public static Page<CommentShowResponse> toList(Page<Comment> comments){
        Page<CommentShowResponse> commentShowResponsePage = comments.map(m -> CommentShowResponse.builder() // 이게 뭐야...
                .id(m.getId())
                .comment(m.getComment())
                .userName(m.getUser().getUserName())
                .postId(m.getPost().getId())
                .createdAt(m.getCreatedAt())
                .build());
        return commentShowResponsePage;
    }
}
