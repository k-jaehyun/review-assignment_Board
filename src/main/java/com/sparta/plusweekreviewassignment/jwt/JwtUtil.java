package com.sparta.plusweekreviewassignment.jwt;

import com.sparta.plusweekreviewassignment.exception.NotFoundException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String sercretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(sercretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String nickname) {
        Date date = new Date();

        long TOKEN_TIME = 60 * 60 * 1000; // 만료시간 60분

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(nickname)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰으로 쿠키 생성
    public Cookie addJwtToCookie(String bearerToken) {
        try {
            String spaceRemovedToken = URLEncoder.encode(bearerToken, "utf-8").replaceAll("\\+", "%20"); // 공백 제거

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, spaceRemovedToken);
            cookie.setPath("/");

            return cookie;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public String substringBearerToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        throw new NotFoundException("Not Found Token");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token Validation Exception");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
