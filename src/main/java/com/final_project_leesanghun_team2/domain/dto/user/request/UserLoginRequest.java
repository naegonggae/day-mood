package com.final_project_leesanghun_team2.domain.dto.user.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @Email(message = "이메일 형식을 맞춰 주세요.")
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String username;

    @Size(min = 5, message = "비밀번호를 최소 5글자 이상으로 입력해주세요.")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
}
