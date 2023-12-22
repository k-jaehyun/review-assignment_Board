package com.sparta.plusweekreviewassignment.User.emailAuth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailService emailService;
    private final EmailAuthRepository emailAuthRepository;
    private final RedisTemplate<String , String > redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public static final String EMAIL_AUTHORIZATION_HEADER = "EmailAuth";

    public void checkAndSendVerificationCode(String newNickname, String newPassword, String email, HttpServletResponse response) {
        // 인증번호 보낸 내역이 있는지 확인
        if (Boolean.TRUE.equals(redisTemplate.hasKey(email))) {
            throw new IllegalArgumentException("해당 이메일 주소로 인증번호가 이미 발송되었습니다.");
        }

        // 인증번호 메일 보내기
        String sentCode = sendVerificationCode(email);

        // redis 활용
        redisTemplate.opsForValue().set(email, sentCode, 5*60*1000, TimeUnit.MILLISECONDS);

        // 쿠키에 인증할 email 주소를 넣어보냄
        Cookie cookie = getCookieByEmail(email);
        setCookie(cookie, response);

        emailAuthRepository.save(new EmailAuth(sentCode, newNickname, passwordEncoder.encode(newPassword), email));
    }

    public EmailAuth verifyVerificationCode(String email, String verificationCode) {
        // 가장 최근에 만들어진 인증 데이터 조회 (5분 이내 인증에 실패했을 경우 중복 생성 될 수 있음)
        var emailAuth = emailAuthRepository.findTopByEmailOrderByCreatedAtDesc(email).orElseThrow(()
                -> new IllegalArgumentException("인증 가능한 이메일 주소가 아닙니다."));

        // 5분이 지났는지 검증
        if (!redisTemplate.hasKey(email)) {
            throw new IllegalArgumentException("5분 초과, 다시 인증하세요");
        }

        // 인증번호 일치하는지 확인
        if (!emailAuth.getSentCode().equals(verificationCode)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        return emailAuth;
    }

    public void endEmailAuth(EmailAuth emailAuth, HttpServletResponse response) {
        redisTemplate.delete(emailAuth.getEmail());
        emailAuthRepository.delete(emailAuth);
        Cookie cookie = new Cookie(EMAIL_AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    private String sendVerificationCode(String email) {
        String generatedCode = generateRandomCode();

        // 이메일로 인증 번호 발송
        emailService.sendVerificationCodeByEmail(email, "회원가입을 위한 인증 번호 메일입니다." , "인증번호: "+generatedCode);
        return generatedCode;
    }

    private Cookie getCookieByEmail(String email){
        Cookie cookie = new Cookie(EMAIL_AUTHORIZATION_HEADER, email);
        cookie.setPath("/");
        cookie.setMaxAge(5*60);
        return cookie;
    }

    private void setCookie(Cookie cookie, HttpServletResponse response) {
        response.addCookie(cookie);
    }

    private String generateRandomCode() {
        // 랜덤한 6자리 숫자 생성
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }



    // 이메일인증 5분 지났는데도 완료되지않은 데이터 삭제
    @Transactional
    @Scheduled(fixedRate = 5 * 60 * 1000) // 5분에 한번 작동
    public void cleanupEmailAuth() {
        LocalDateTime fiveMinAgo = LocalDateTime.now().minusMinutes(5);
        emailAuthRepository.deleteByCreatedAtBefore(fiveMinAgo);
    }

}

