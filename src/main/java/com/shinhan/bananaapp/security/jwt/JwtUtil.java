package com.shinhan.bananaapp.security.jwt;


import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey key;               // Key → SecretKey (0.12.x)
    private final long accessTokenExpMin;
    private final long refreshTokenExpDay;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expiration_minute}") long accessTokenExpMin,
                   @Value("${jwt.refresh_expiration_day}") long refreshTokenExpDay) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpMin  = accessTokenExpMin;
        this.refreshTokenExpDay = refreshTokenExpDay;
    }

    // ── Access Token 생성 (외부 호출용) ──────────
    public String createAccessToken(MemberEntity member) {
        return createToken(member, accessTokenExpMin);
    }

    // ── JWT 토큰 생성 (내부) ──────────────────────
    private String createToken(MemberEntity member, long expireMin) {

        ZonedDateTime now           = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireMin * 60);

        return Jwts.builder()
                // etClaims() → claim() 개별 추가 (0.12.x)
                .claim("memberId",   member.getMid())
                .claim("mname",      member.getMname())
                .claim("mrole",      member.getMrole())
                .claim("token_type", "access")
                //setIssuedAt() → issuedAt()
                .issuedAt(Date.from(now.toInstant()))
                //setExpiration() → expiration()
                .expiration(Date.from(tokenValidity.toInstant()))
                // signWith(key, algorithm) → signWith(key) 알고리즘 자동
                .signWith(key)
                .compact();
    }

    // ── Token에서 사용자 ID 추출 ──────────────────
    public String getUserId(String token) {
        return parseClaims(token).get("memberId", String.class);
    }

    // ── JWT 검증 (서명·만료·위조 확인) ────────────
    public boolean validateToken(String token) {
        try {
            // parserBuilder() → parser()
            // setSigningKey()  → verifyWith()
            // parseClaimsJws() → parseSignedClaims()
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new BadCredentialsException("INVALID_JWT", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new BadCredentialsException("TOKEN_EXPIRED", e);
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            log.info("JWT error: {}", e.getMessage());
        }
        return false;
    }

    // ── JWT Claims 추출 ───────────────────────────
    // 만료 토큰도 Claims 추출 가능 — Refresh Token 검증에 유리
    public Claims parseClaims(String token) {
        try {
            // parseClaimsJws() → parseSignedClaims()
            // getBody()        → getPayload()
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료되어도 Claims 반환
        }
    }

    // ── Refresh Token 생성 ────────────────────────
    public String generateRefreshToken(String memberId) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshTokenExpDay * 24 * 3600);

        return Jwts.builder()
                .claim("memberId",   memberId)
                .claim("token_type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    // ── Refresh Token 판별 ────────────────────────
    public boolean isRefreshToken(String jwt) {
        return "refresh".equals(parseClaims(jwt).get("token_type"));
    }

    // JwtUtil에 테스트용 만료 토큰 생성 메서드 추가
    public String generateExpiredRefreshToken(String mid) {
        return Jwts.builder()
                .claim("memberId",   mid)
                .claim("token_type", "refresh")
                .issuedAt(new Date(System.currentTimeMillis() - 10000))
                // ↑ 10초 전 발급
                .expiration(new Date(System.currentTimeMillis() - 1000))
                // ↑ 1초 전 만료 → 이미 만료된 토큰
                .signWith(key)
                .compact();
    }

}

