package com.example.DayBridge.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoinRequest {
    @NotEmpty(message = "아이디를 입력해주세요")
    private String userID;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String userPW;
    private String pwCheck;

    @NotEmpty(message = "사용하실 닉네임을 입력해주세요")
    private String nickName;

    @NotBlank(message = "이메일을 입력해주세요")
    private String Email;

    @NotBlank(message = "이름을 입력해주세요")
    private String Name;

    public Users toEntity(String encode) {
        return Users.builder()
                .userID(this.userID)
                .userPW(encode)
                .nickName(this.nickName)
                .email(this.Email)
                .name(this.Name)
                .build();
    }
}
