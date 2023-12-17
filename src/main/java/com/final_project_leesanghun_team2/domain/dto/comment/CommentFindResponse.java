package com.final_project_leesanghun_team2.domain.dto.comment;

import com.final_project_leesanghun_team2.domain.entity.Comment;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CommentFindResponse {

    private Long id;
    private String content;
    private Long userId;
    private String nickName;
    private String updatedAt;
    public static CommentFindResponse from(Comment comment){
        return new CommentFindResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getNickName(),
                makeDay(comment.getUpdatedAt()));
    }

    // 댓글을 남긴 시간 계산 메서드
    private static String makeDay(LocalDateTime updatedAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(updatedAt, now);

        log.info(String.valueOf(duration));
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        log.info(String.valueOf(days));
        log.info(String.valueOf(hours));
        log.info(String.valueOf(minutes));

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