package com.final_project_leesanghun_team2.domain.dto.comment;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyFindResponse {

    private Long id;
    private String content;
    private Long userId;
    private String nickName;
    private String updatedAt;

    public static ReplyFindResponse from(Comment comment){
        return new ReplyFindResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getNickName(),
                calculateTimeAgo(comment.getUpdatedAt()));
    }

    // 답글을 남긴 시간 계산 메서드
    private static String calculateTimeAgo(LocalDateTime updatedAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(updatedAt, now);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else if (minutes > 0) {
            return minutes + "분 전";
        } else {
            return "방금 전";
        }
    }

}
