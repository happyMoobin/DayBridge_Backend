package com.example.DayBridge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @NotBlank(message = "별명을 입력해주세요")
    private String nickName;
    @NotBlank(message = "아이디를 입력해주세요")
    private String userID;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String userPW;
}
