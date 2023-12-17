package com.final_project_leesanghun_team2.domain.dto.tag;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagSaveRequest {

	@NotBlank(message = "태그가 입력되지 않았습니다.")
	private String name;
}
