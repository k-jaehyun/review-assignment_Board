package com.sparta.plusweekreviewassignment.User;

import com.sparta.plusweekreviewassignment.common.CommonResponseDto;
import com.sparta.plusweekreviewassignment.User.dto.LoginRequestDto;
import com.sparta.plusweekreviewassignment.User.dto.SignupRequestDto;
import com.sparta.plusweekreviewassignment.exception.fieldError.FieldErrorDto;
import com.sparta.plusweekreviewassignment.exception.fieldError.FieldErrorException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserContoroller {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // validation검증
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        if (!fieldErrorList.isEmpty()) {
            List<FieldErrorDto> fieldErrorDtoList = fieldErrorList.stream().map(FieldErrorDto::new).toList();
            throw new FieldErrorException("허용되지 않은 입력값입니다.",HttpStatus.BAD_REQUEST.value(),fieldErrorDtoList);
        }

        // 회원가입 로직 (인증 메일 발송)
        userService.signup(requestDto);

        return ResponseEntity.ok().body(new CommonResponseDto("인증번호를 입력해주세요!", HttpStatus.OK.value()));
    }

    @GetMapping("/signup/email/{email}/code/{verificationCode}")
    public ResponseEntity<CommonResponseDto> verificateCode(@PathVariable String email,
                                                            @PathVariable String verificationCode) {
        String nickname = userService.verificateCode(email, verificationCode);
        return ResponseEntity.ok().body(new CommonResponseDto(nickname+"님 회원가입 완료.",HttpStatus.OK.value()));
    }

    // nickname 중복여부 확인
    @GetMapping("/signup/{nickname}")
    public String checkNickname(@PathVariable String nickname) {
        return userService.checkNickName(nickname);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        userService.login(requestDto, response);
        return ResponseEntity.ok().body(new CommonResponseDto(requestDto.getNickname()+"님, 환영합니다!", HttpStatus.OK.value()));
    }
}
