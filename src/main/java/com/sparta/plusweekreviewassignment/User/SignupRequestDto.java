package com.sparta.plusweekreviewassignment.User;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 입력")
    @Size(min = 3, message = "최소 3자 이상 입력")
    private String nickname;

    @Size(min = 4, message = "최소 4자 이상 입력")
    @NotBlank
    private String password;

    @NotBlank
    private String passwordCheck;

}
