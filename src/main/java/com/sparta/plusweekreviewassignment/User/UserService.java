package com.sparta.plusweekreviewassignment.User;

import com.sparta.plusweekreviewassignment.User.dto.LoginRequestDto;
import com.sparta.plusweekreviewassignment.User.dto.SignupRequestDto;
import com.sparta.plusweekreviewassignment.User.emailAuth.EmailAuth;
import com.sparta.plusweekreviewassignment.User.emailAuth.EmailAuthRepository;
import com.sparta.plusweekreviewassignment.User.emailAuth.EmailAuthService;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailAuthService emailAuthService;
    private final EmailAuthRepository emailAuthRepository;
    private final RedisTemplate<String , String > redisTemplate;

    public void signup(SignupRequestDto requestDto) {
        String newNickname= requestDto.getNickname();
        String newPassword= requestDto.getPassword();
        String newPasswordCheck= requestDto.getPasswordCheck();
        String email = requestDto.getEmail();

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

        // 인증번호 메일 보내기
        String sentCode = emailAuthService.sendVerificationCode(email);

        // redis 활용
        redisTemplate.opsForValue().set(email, sentCode, 5*60*1000, TimeUnit.MILLISECONDS);

        emailAuthRepository.save(new EmailAuth(sentCode, newNickname, passwordEncoder.encode(newPassword), email));
    }

    // 이메일 인증 및 User 생성
    public String verificateCode(String email, String verificateCode) {
        var emailAuth = emailAuthRepository.findByEmail(email).orElseThrow(()
                -> new IllegalArgumentException("인증 가능한 이메일 주소가 아닙니다."));

        // 5분이 지났는지 검증
        if (redisTemplate.hasKey(email)==null) {
            redisTemplate.delete(email);
            throw new IllegalArgumentException("5분 초과, 다시 인증하세요");
        }

        String nickname = emailAuth.getNickname();
        String password = emailAuth.getPassword();

        emailAuthService.verifyVerificationCode(email,verificateCode);

        userRepository.save(new User(nickname,password,email));

        //인증 완료되면 삭제
        emailAuthRepository.delete(emailAuth);
        redisTemplate.delete(email);
        return nickname;
    }

    public String checkNickName(String nickname) {
        if(userRepository.findByNickname(nickname).isEmpty()) {
            return "사용 가능한 닉네임";
        } else {
            return "사용 중인 닉네임";
        }
    }

    // 로그인
    public void login(LoginRequestDto requestDto, HttpServletResponse response) {
        // 아이디, 패스워드 확인
        User user = userRepository.findByNickname(requestDto.getNickname()).orElse(null);
        if (user==null || !(passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))) {
            throw new IllegalArgumentException("닉네임 또는 패스워드를 확인해주세요");
        }

        // 토큰 발급, 쿠키 생성, 쿠키를 헤더에 전달
        String bearerToken = jwtUtil.createToken(user.getNickname());
        Cookie cookie = jwtUtil.addJwtToCookie(bearerToken);
        response.addCookie(cookie);
    }
}
