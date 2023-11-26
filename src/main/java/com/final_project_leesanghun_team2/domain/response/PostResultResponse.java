package com.final_project_leesanghun_team2.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResultResponse {
    private String message;
    private Integer postId;

    // 포스트 등록할때 PostResultResponse 에 DB에 저장된 post_id 연결
    public static PostResultResponse ofPost(Integer postId) {
        return new PostResultResponse(
                "포스트 등록 완료",
                postId
        );
    }
    // 포스트 수정할때 PostResultResponse 에 DB에 저장된 post_id 연결
    public static PostResultResponse ofModifyPost(Integer postId) {
        return new PostResultResponse(
                "포스트 수정 완료",
                postId
        );
    }

    // 포스트 삭제할때 PostResultResponse 에 DB에 저장된 post_id 연결
    public static PostResultResponse ofDeletePost(Integer postId) {
        return new PostResultResponse(
                "포스트 삭제 완료",
                postId
        );
    }
}
