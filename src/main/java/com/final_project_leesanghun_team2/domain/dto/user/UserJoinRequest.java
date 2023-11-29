package com.final_project_leesanghun_team2.domain.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {

    @Email(message = "이메일 형식을 맞춰 주세요")
    @NotBlank(message = "이메일을 입력해 주세요")
    private String username;
    @Min(value = 4, message = "비밀번호는 4글자보다 길게 설정해주세요")
    @NotBlank
    private String password;
    @NotBlank
    @Size(min = 2, max = 10)
    private String nickName;
}
