package com.sparta.plusweekreviewassignment.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public String signup(SignupRequestDto requestDto) {
        String newNickname= requestDto.getNickname();
        String newPassword= requestDto.getPassword();
        String newPasswordCheck= requestDto.getPasswordCheck();

        // nickname 중복여부 확인
        if(!userRepository.findByNickname(newNickname).isEmpty()) {
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }

        // password 확인
        // 1. nickname과 같은 값이 포함됐는지
        if (newPassword.contains(newNickname)) {
            throw new IllegalArgumentException("비밀번호에 닉네임과 같은 값이 포함될 수 없습니다.");
        }
        // 2. 비밀번호 확인이 비밀번호와 일치하는지
        if (!newPassword.equals(newPasswordCheck)) {
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // User 저장
        userRepository.save(new User(newNickname,newPassword));

        return newNickname;
    }
}
