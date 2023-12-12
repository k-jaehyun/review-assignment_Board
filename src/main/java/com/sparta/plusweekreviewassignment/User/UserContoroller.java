package com.sparta.plusweekreviewassignment.User;

import com.sparta.plusweekreviewassignment.CommonResponseDto;
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

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // validation검증
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        if (!fieldErrorList.isEmpty()) {
            return ResponseEntity.badRequest().body(new CommonResponseDto("입력값 오류",HttpStatus.BAD_REQUEST.value()));
        }

        // 회원가입 로직
        String signupedNickname = userService.signup(requestDto);

        return ResponseEntity.ok().body(new CommonResponseDto(signupedNickname+"님, 회원가입 완료!", HttpStatus.OK.value()));
    }
}
