package com.final_project_leesanghun_team2.domain.dto.user.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

	@Size(min = 2, max = 10, message = "닉네임은 최소 2글자 이상 10글자 이하로 입력해주세요.")
	@NotBlank(message = "닉네임을 입력해 주세요.")
	private String nickName;

}
