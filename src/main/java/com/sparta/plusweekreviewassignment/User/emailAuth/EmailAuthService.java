package com.sparta.plusweekreviewassignment.User.emailAuth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailService emailService;
    private final EmailAuthRepository emailAuthRepository;


    public String sendVerificationCode(String email) {
        String sentCode = generateRandomCode();

        // 이메일로 인증 번호 발송
        emailService.sendVerificationCodeByEmail(email, sentCode);
        return sentCode;
    }

    public void verifyVerificationCode(String email, String verificationCode) {
        // 저장된 인증 번호와 사용자가 입력한 인증 번호를 비교
        var emailAuth = emailAuthRepository.findByEmail(email).orElse(null);
        if (emailAuth!=null && !emailAuth.getSentCode().equals(verificationCode)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
    }

    private String generateRandomCode() {
        // 랜덤한 6자리 숫자 생성
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}

