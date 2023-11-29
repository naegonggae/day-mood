package com.final_project_leesanghun_team2.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.final_project_leesanghun_team2.domain.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;
    private String username;
    private String nickName;
    @JsonFormat(pattern = "yyyy년 M월 d일")
    private LocalDateTime createdAt;

    public static UserJoinResponse from(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getCreatedAt()
        );
    }
}
