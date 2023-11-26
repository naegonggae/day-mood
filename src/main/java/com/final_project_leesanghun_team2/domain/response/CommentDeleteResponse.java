package com.final_project_leesanghun_team2.domain.response;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDeleteResponse {
    private String message;
    private Integer id;

    // 댓글 삭제할때 Comment -> CommentDeleteResponse 으로 포장
    public static CommentDeleteResponse of(Comment comment) {
        return new CommentDeleteResponse(
                "댓글 삭제 완료",
                comment.getId()
        );
    }

}
