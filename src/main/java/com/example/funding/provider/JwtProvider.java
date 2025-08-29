package com.example.funding.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * <p>JWT 토큰 생성 및 검증을 담당하는 컴포넌트</p>
 * @since 2025-08-26
 * @author 장민규
 */
@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * <p>JWT 액세스 토큰 생성</p>
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @param role 사용자 역할
     * @return 생성된 JWT 토큰
     * @since 2025-08-26
     * @author 장민규
     */
    public String createAccessToken(Long userId, String email, String role) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().subject(email)
                .claim("email", email)
                .claim("userId", userId)
                .claim("role", role)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * <p>JWT 토큰 검증 및 클레임 추출</p>
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효한 경우 클레임 반환, 유효하지 않은 경우 예외 발생
     * @throws ExpiredJwtException 토큰이 만료된 경우
     * @throws JwtException 기타 토큰 관련 예외 발생 시
     * @since 2025-08-26
     * @author 장민규
     */
    public Claims validateAndGetClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
