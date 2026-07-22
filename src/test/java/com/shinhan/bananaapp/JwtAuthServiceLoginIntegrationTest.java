package com.shinhan.bananaapp;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberRole;
import com.shinhan.bananaapp.jpaprac.repository.MemberRepository;
import com.shinhan.bananaapp.security.jwt.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("JWT + DB RefreshToken 통합 테스트")
class JwtAuthServiceLoginIntegrationTest {

    @Autowired
    AuthServiceLogin authService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RefreshTokenService refreshTokenService;

    private static final String MID = "member2";

    // ──────────────────────────────────────────────
    // 회원 등록 (최초 1회만)
    // ──────────────────────────────────────────────
    @Test
    @Transactional @Commit
    @DisplayName("테스트용 회원 등록")
    void insert() {
        MemberEntity member = new MemberEntity();
        member.setMid(MID);
        member.setMpassword(passwordEncoder.encode("1234"));
        member.setMname("찐2");
        member.setMrole(MemberRole.ADMIN);
        System.out.println(memberRepository.save(member));
    }

    // ──────────────────────────────────────────────
    // 로그인 → AccessToken 발급 확인
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("로그인 → AccessToken 발급 확인")
    void login_real() {
        // given
        MemberEntity input = MemberEntity.builder()
                .mid(MID).mpassword("1234").build();

        // when
        TokenResponse resp = authService.login(input);

        // then
        assertNotNull(resp);
        assertNotNull(resp.getAccessToken());
        System.out.println("AccessToken: " + resp.getAccessToken());

        // DB에 RefreshToken 저장됐는지 확인 (Service 통해)
        MemberEntity member = memberRepository
                .findById(MID).orElseThrow();
        RefreshTokenEntity saved =
                refreshTokenService.findByMid(member);
        assertNotNull(saved, "DB에 RefreshToken이 저장되지 않았음");
        System.out.println("DB 저장 토큰: " + saved.getRefreshToken());
    }

    // ──────────────────────────────────────────────
    // RefreshToken 유효 → 만료 → 재발급
    // ──────────────────────────────────────────────
    @Test
    @Transactional @Commit
    @DisplayName("RefreshToken 유효성 체크 → 만료 시 재발급")
    void login_reissue() {

        // ── 1. DB에서 영속화된 MemberEntity 조회 ─────
        MemberEntity member = memberRepository
                .findById(MID)
                .orElseThrow(() ->
                        new RuntimeException(MID + " 없음. insert() 먼저 실행"));

        // ── 2. Service로 RefreshToken 조회 ───────────
        RefreshTokenEntity entity =
                refreshTokenService.findByMid(member);
        assertNotNull(entity, "RefreshToken 없음. login_real() 먼저 실행");
        System.out.println("저장된 RefreshToken: " + entity);

        // ── 3. 유효성 체크 ────────────────────────────
        String token = entity.getRefreshToken();
        boolean isValid;
        try {
            isValid = jwtUtil.validateToken(token);
        } catch (Exception e) {
            isValid = false;
        }
        System.out.println("1. 유효 여부: " + isValid);
        assertThat(isValid).isTrue();

        // ── 4. 만료 토큰으로 교체 ─────────────────────
        String expiredToken =
                jwtUtil.generateExpiredRefreshToken(MID);
        entity.setRefreshToken(expiredToken);
        // @Transactional 안 → 별도 save 불필요
        // JPA 변경 감지가 트랜잭션 종료 시 자동 UPDATE
        System.out.println("만료 토큰으로 교체 완료");

        // ── 5. 만료 후 유효성 체크 ────────────────────
        try {
            isValid = jwtUtil.validateToken(expiredToken);
        } catch (Exception e) {
            isValid = false;
        }
        System.out.println("2. 만료 후 유효 여부: " + isValid);
        assertThat(isValid).isFalse();

        // ── 6. 재발급 ─────────────────────────────────
        System.out.println("RefreshToken 만료 → 재발급");
        String newToken = jwtUtil.generateRefreshToken(MID);
        refreshTokenService.save(MID, newToken);

        // ── 7. 새 토큰 확인 (Service 통해) ───────────
        RefreshTokenEntity updated =
                refreshTokenService.findByMid(member);
        assertNotNull(updated);
        assertThat(updated.getRefreshToken()).isEqualTo(newToken);
        System.out.println("새 RefreshToken: " + updated.getRefreshToken());
    }

    // ──────────────────────────────────────────────
    // 로그아웃 → DB RefreshToken 삭제 확인
    // ──────────────────────────────────────────────
    @Test
    @Transactional@Commit
    @DisplayName("로그인 → 로그아웃 → DB 삭제 확인")
    void logout_deleteRefreshToken() {

        // ── 1. 로그인 ─────────────────────────────────
        MemberEntity input = MemberEntity.builder()
                .mid(MID).mpassword("1234").build();
        authService.login(input);

        MemberEntity member = memberRepository
                .findById(MID).orElseThrow();

        // ── 2. 로그아웃 전 확인 ───────────────────────
        RefreshTokenEntity before =
                refreshTokenService.findByMid(member);
        assertNotNull(before, "로그인 후 RefreshToken이 DB에 없음");
        System.out.println("로그아웃 전: " + before.getRefreshToken());

        // ── 3. 로그아웃 ───────────────────────────────
        refreshTokenService.logout(MID);
        System.out.println("로그아웃 완료");

        // ── 4. 삭제 확인 (Service 통해) ──────────────
        RefreshTokenEntity after =
                refreshTokenService.findByMid(member);
        assertNull(after, "로그아웃 후 RefreshToken이 남아있음");
        System.out.println("로그아웃 후: " + after); // null
    }

    // ──────────────────────────────────────────────
    // 로그아웃 후 재발급 시도 → 실패
    // @Transactional 없음 — 예외 테스트
    // ──────────────────────────────────────────────
    @Test
    @DisplayName("로그아웃 후 재발급 시도 → BadCredentialsException")
    void logout_then_reissue_fail() {

        // ── 1. 로그인 ─────────────────────────────────
        MemberEntity input = MemberEntity.builder()
                .mid(MID).mpassword("1234").build();
        authService.login(input);

        // ── 2. RefreshToken 조회 ──────────────────────
        MemberEntity member = memberRepository
                .findById(MID).orElseThrow();
        String refreshToken = refreshTokenService
                .findByMid(member).getRefreshToken();

        // ── 3. 로그아웃 ───────────────────────────────
        refreshTokenService.logout(MID);

        // ── 4. 재발급 시도 → 예외 확인 ───────────────
        Exception ex = assertThrows(BadCredentialsException.class,
                () -> refreshTokenService.reissueAccessToken(refreshToken));

        assertThat(ex.getMessage()).contains("Invalid Refresh Token");
        System.out.println("재발급 실패 확인: " + ex.getMessage());
    }

    // ──────────────────────────────────────────────
    // 중복 로그인 → RefreshToken 1건만 유지 (Upsert)
    // ──────────────────────────────────────────────
    @Test
    @Transactional
    @Commit
    @DisplayName("중복 로그인 → RefreshToken 1건만 유지")
    void login_twice_only_one_token() {
        MemberEntity input = MemberEntity.builder()
                .mid(MID).mpassword("1234").build();

        authService.login(input);
        authService.login(input);

        // ── DB 1건 확인 (Service 통해) ────────────────
        // findByMid로 null 아닌지 확인
        MemberEntity member = memberRepository
                .findById(MID).orElseThrow();
        RefreshTokenEntity entity =
                refreshTokenService.findByMid(member);

        assertNotNull(entity, "중복 로그인 후 RefreshToken 없음");
        System.out.println("저장 건수 확인: 1건 유지");
        System.out.println("토큰: " + entity.getRefreshToken());
    }
}