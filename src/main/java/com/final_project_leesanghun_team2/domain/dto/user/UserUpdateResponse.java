package com.final_project_leesanghun_team2.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.final_project_leesanghun_team2.domain.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateResponse {

	private Long id;
	private String nickName;
	@JsonFormat(pattern = "yyyy년 M월 d일 H시 m분 s초 S밀리세컨")
	private LocalDateTime createdAt;
	@JsonFormat(pattern = "yyyy년 M월 d일 H시 m분 s초") // 업데이트 시간 반영 안됨
	private LocalDateTime updatedAt;

	public static UserUpdateResponse from(User user) {
		return new UserUpdateResponse(user.getId(), user.getNickName(), user.getCreatedAt(), user.getUpdatedAt());
	}

}
