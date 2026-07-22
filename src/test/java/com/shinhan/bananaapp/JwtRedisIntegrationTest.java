//package com.shinhan.bananaapp;
//
//import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
//import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberRole;
//import com.shinhan.bananaapp.jpaprac.repository.MemberRepository;
//import com.shinhan.bananaapp.security.jwt.JwtUtil;
//import com.shinhan.bananaapp.security.jwt.TokenResponse;
////import com.shinhan.bananaapp.security.redis.AuthServiceLoginRedis;
////import com.shinhan.bananaapp.security.redis.RefreshTokenServiceRedis;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@DisplayName("JWT + Redis RefreshToken 통합 테스트")
//class JwtRedisIntegrationTest {
//
////    @Autowired
////    AuthServiceLoginRedis authService;
////    @Autowired
////    RefreshTokenServiceRedis refreshTokenService;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    PasswordEncoder passwordEncoder;
//    @Autowired
//    JwtUtil jwtUtil;
//    @Autowired
//    RedisTemplate<String, String> redisTemplate;
//
//    // ──────────────────────────────────────────────
//    // 테스트용 회원 등록 (최초 1회만 실행)
//    // ──────────────────────────────────────────────
//    @Test
//    @Transactional
//    @Commit
//    @DisplayName("테스트용 회원 등록")
//    void insert() {
//        MemberEntity member = new MemberEntity();
//        member.setMid("member1");
//        member.setMpassword(passwordEncoder.encode("1234"));
//        member.setMname("찐1");
//        member.setMrole(MemberRole.ADMIN);
//        System.out.println(memberRepository.save(member));
//    }
//
//    // ──────────────────────────────────────────────
//    // Redis 연결 확인
//    // ──────────────────────────────────────────────
//    @Test
//    @DisplayName("Redis 연결 확인")
//    void redis_connection() {
//        redisTemplate.opsForValue().set("ping", "pong");
//        String result = redisTemplate.opsForValue().get("ping");
//        assertThat(result).isEqualTo("pong");
//        System.out.println("Redis 연결 정상: " + result);
//    }
//
//    // ──────────────────────────────────────────────
//    // 로그인 → AccessToken + Redis RefreshToken 확인
//    // ──────────────────────────────────────────────
//    @Test
//    @DisplayName("로그인 → AccessToken 발급 + Redis RefreshToken 저장 확인")
//    void login_real() {
//        // given
//        MemberEntity input = MemberEntity.builder()
//                .mid("member1").mpassword("1234").build();
//
//        // when
//        TokenResponse resp = authService.login(input);
//
//        // then — AccessToken 확인
//        assertNotNull(resp);
//        assertNotNull(resp.getAccessToken());
//        System.out.println("AccessToken: " + resp.getAccessToken());
//
//        // Redis 저장 확인
//        // AuthServiceLoginRedis.login() 에서
//        // refreshTokenServiceRedis.save(mid, token) 호출
//        MemberEntity member = memberRepository
//                .findById("member1").orElseThrow();
//        String stored = refreshTokenService.findByMid(member);
//        assertNotNull(stored, "Redis에 RefreshToken이 저장되지 않았음");
//        System.out.println("Redis 저장 토큰: " + stored);
//
//        // TTL 확인 (7일)
//        Long ttl = redisTemplate.getExpire(
//                "refresh:member1", TimeUnit.SECONDS);
//        assertThat(ttl).isGreaterThan(0L);
//        System.out.println("TTL(초): " + ttl);
//    }
//
//    // ──────────────────────────────────────────────
//    // RefreshToken 유효 → 만료 → 재발급
//    // ──────────────────────────────────────────────
//    @Test
//    @DisplayName("RefreshToken 유효성 체크 → 만료 시 재발급")
//    void login_reissue() throws Exception {
//
//        // ── 1. DB에서 영속화된 MemberEntity 조회 ─────
//        MemberEntity member = memberRepository
//                .findById("member1")
//                .orElseThrow(() ->
//                        new RuntimeException("member1 없음. insert() 먼저 실행"));
//
//        // ── 2. Redis에서 RefreshToken 조회 ───────────
//        // findByMid(MemberEntity) → String 반환
//        String token = refreshTokenService.findByMid(member);
//        assertNotNull(token, "RefreshToken 없음. login_real() 먼저 실행");
//        System.out.println("저장된 RefreshToken: " + token);
//
//        // ── 3. 유효성 체크 ────────────────────────────
//        boolean isValid;
//        try {
//            isValid = jwtUtil.validateToken(token);
//        } catch (Exception e) {
//            isValid = false;
//        }
//        System.out.println("1. 유효 여부: " + isValid);
//        assertThat(isValid).isTrue();
//
//        // ── 4. 만료 토큰으로 Redis 강제 교체 ─────────
//        // TTL 1초로 설정 → 1.5초 후 자동 만료
//        String expiredToken =
//                jwtUtil.generateExpiredRefreshToken("member1");
//        redisTemplate.opsForValue()
//                .set("refresh:member1", expiredToken, 1, TimeUnit.SECONDS);
//        System.out.println("만료 토큰 교체 완료");
//
//        Thread.sleep(1500); // 1.5초 대기 → TTL 만료
//
//        // ── 5. 만료 후 유효성 체크 ───────────────────
//        // TTL 만료 → findByMid() → null 반환
//        String expiredCheck = refreshTokenService.findByMid(member);
//        isValid = (expiredCheck != null);
//        System.out.println("2. 만료 후 유효 여부: " + isValid); // false
//        assertThat(isValid).isFalse();
//
//        // ── 6. 재발급 ─────────────────────────────────
//        System.out.println("RefreshToken 만료 → 재발급");
//        String newToken = jwtUtil.generateRefreshToken("member1");
//        refreshTokenService.save("member1", newToken);
//
//        // ── 7. 새 토큰 확인 ───────────────────────────
//        String updated = refreshTokenService.findByMid(member);
//        assertNotNull(updated);
//        assertThat(updated).isEqualTo(newToken);
//        System.out.println("새 RefreshToken: " + updated);
//
//        Long newTtl = redisTemplate.getExpire(
//                "refresh:member1", TimeUnit.SECONDS);
//        assertThat(newTtl).isGreaterThan(0L);
//        System.out.println("새 TTL(초): " + newTtl);
//    }
//
//    // ──────────────────────────────────────────────
//    // reissueAccessToken — 정상 재발급
//    // ──────────────────────────────────────────────
//    @Test
//    @DisplayName("reissueAccessToken → 새 AccessToken + Rotation 확인")
//    void reissue_success() {
//        // given — 로그인
//        MemberEntity input = MemberEntity.builder()
//                .mid("member1").mpassword("1234").build();
//        authService.login(input);
//
//        // ── 2. 로그인 직후 Redis 토큰 직접 확인 ──────
//        // findByMid 대신 redisTemplate 직접 조회
//        String oldToken = redisTemplate.opsForValue()
//                .get("refresh:member1");
//        assertNotNull(oldToken, "로그인 후 Redis에 토큰 없음");
//        System.out.println("로그인 직후 토큰: " + oldToken);
//
//        // ── 3. 재발급 ─────────────────────────────────
//        TokenResponse newResp =
//                refreshTokenService.reissueAccessToken(oldToken);
//
//        assertNotNull(newResp.getAccessToken());
//        System.out.println("새 AccessToken: " + newResp.getAccessToken());
//
//        // ── 4. Rotation 확인 ──────────────────────────
//        // 재발급 후 Redis 토큰이 변경됐는지 직접 확인
//        String rotated = redisTemplate.opsForValue()
//                .get("refresh:member1");
//
//        System.out.println("이전 토큰: " + oldToken);
//        System.out.println("이후 토큰: " + rotated);
//
//        assertThat(rotated).isNotNull();
//        assertThat(rotated).isNotEqualTo(oldToken); // Rotation 확인
//    }
//
//    // ──────────────────────────────────────────────
//    // reissueAccessToken — 위조 토큰 → 실패
//    // ──────────────────────────────────────────────
//    @Test
//    @DisplayName("위조 토큰으로 재발급 시도 → BadCredentialsException")
//    void reissue_fakeToken() {
//        // assertThrows — @Transactional 없음
//        Exception ex = assertThrows(Exception.class,
//                () -> refreshTokenService
//                        .reissueAccessToken("fake.token.value"));
//
//        System.out.println("예외 확인: " + ex.getMessage());
//    }
//
//    // ──────────────────────────────────────────────
//    // 로그아웃 → Redis 삭제 확인
//    // ──────────────────────────────────────────────
//    @Test
//    @DisplayName("로그아웃 → Redis RefreshToken 삭제 확인")
//    void logout_test() {
//        // given — 로그인
//        MemberEntity input = MemberEntity.builder()
//                .mid("member1").mpassword("1234").build();
//        authService.login(input);
//
//        // Redis 저장 확인
//        String before = redisTemplate.opsForValue()
//                .get("refresh:member1");
//        assertNotNull(before);
//        System.out.println("로그아웃 전: " + before);
//
//        // when — 로그아웃
//        authService.logout("member1");
//
//        // then — Redis 삭제 확인
//        // RefreshTokenServiceRedis.logout():
//        //   redisTemplate.delete("refresh:" + mid)
//        String after = redisTemplate.opsForValue()
//                .get("refresh:member1");
//        assertNull(after);
//        System.out.println("로그아웃 후: " + after); // null
//    }
//
//    // ──────────────────────────────────────────────
//    // 로그아웃 후 재발급 시도 → 실패
//    // ──────────────────────────────────────────────
//    @Test
//    // @Transactional 없음 — 예외 테스트
//    @DisplayName("로그아웃 후 재발급 시도 → 실패")
//    void logout_then_reissue_fail() {
//        // given — 로그인
//        MemberEntity input = MemberEntity.builder()
//                .mid("member1").mpassword("1234").build();
//        authService.login(input);
//
//        MemberEntity member = memberRepository
//                .findById("member1").orElseThrow();
//        String refreshToken = refreshTokenService.findByMid(member);
//        assertNotNull(refreshToken);
//
//        // when — 로그아웃
//        authService.logout("member1");
//
//        // then — 재발급 시도 → stored == null → 예외
//        Exception ex = assertThrows(BadCredentialsException.class,
//                () -> refreshTokenService
//                        .reissueAccessToken(refreshToken));
//
//        assertThat(ex.getMessage())
//                .isEqualTo("Invalid Refresh Token");
//        System.out.println("재발급 실패 확인: " + ex.getMessage());
//    }
//}