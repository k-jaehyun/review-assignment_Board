package com.sparta.plusweekreviewassignment.User.emailAuth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailService emailService;
    private final Map<String, String> verificationCodes = new HashMap<>();

    public void sendVerificationCode(String email) {
        String verificationCode = generateRandomCode();
        verificationCodes.put(email, verificationCode);

        // 이메일로 인증 번호 발송
        emailService.sendVerificationCodeByEmail(email, verificationCode);
    }

    public boolean verifyVerificationCode(String email, String verificationCode) {
        // 저장된 인증 번호와 사용자가 입력한 인증 번호를 비교
        String storedVerificationCode = verificationCodes.get(email);
        return storedVerificationCode != null && storedVerificationCode.equals(verificationCode);
    }

    private String generateRandomCode() {
        // 랜덤한 6자리 숫자 생성
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}

