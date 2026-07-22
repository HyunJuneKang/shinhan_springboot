//package com.shinhan.bananaapp.security.redis;
//
//import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
//import com.shinhan.bananaapp.jpaprac.repository.MemberRepository;
//import com.shinhan.bananaapp.security.jwt.JwtUtil;
//import com.shinhan.bananaapp.security.jwt.TokenResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.TimeUnit;
//
//@Service
//@RequiredArgsConstructor
//public class RefreshTokenServiceRedis {
//
//    //X
//    //private final RefreshTokenRepository refreshTokenRepository;
//    private final MemberRepository memberRepository;
//
//    //추가
//    private final RedisTemplate<String, String> redisTemplate;
//    private final JwtUtil jwtUtil;
//
//    //X
////  public RefreshTokenEntity findByMid(MemberEntity member) {
////        return refreshTokenRepository.findByMember(member);
////    }
//
//    //변경
//    public String findByMid(MemberEntity member) {
//        return redisTemplate.opsForValue()
//                .get("refresh:" + member.getMid());
//    }
//
//    // 로그인 성공 시 RefreshToken DB 저장
////    public void save(String mid, String refreshToken) {
////        MemberEntity member = memberRepository.findById(mid)
////                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
////        // 기존 토큰 조회
////        RefreshTokenEntity entity =
////                refreshTokenRepository.findByMember(member);
////
////        if (entity != null) {
////            // 기존 토큰 UPDATE
////            entity.setRefreshToken(refreshToken);
////            refreshTokenRepository.save(entity);
////        } else {
////            // 신규 INSERT
////            refreshTokenRepository.save(
////                    RefreshTokenEntity.builder()
////                            .member(member)
////                            .refreshToken(refreshToken)
////                            .build()
////            );
////        }
////    }
//
//    // 변경 — Redis 한 줄
//    public void save(String mid, String refreshToken) {
//        redisTemplate.opsForValue()
//                .set("refresh:" + mid, refreshToken, 7, TimeUnit.DAYS);
//    }
//
//    // RefreshToken 검증 + AccessToken 재발급
////    @Transactional
////    public TokenResponse reissueAccessToken(String refreshToken) {
////        RefreshTokenEntity tokenEntity =
////                refreshTokenRepository.findByRefreshToken(refreshToken)
////                        .orElseThrow(() -> new BadCredentialsException("Invalid Refresh Token"));
////
////        if (!jwtUtil.validateToken(refreshToken))
////            throw new BadCredentialsException("Refresh Token Invalid");
////        if (!jwtUtil.isRefreshToken(refreshToken))
////            throw new BadCredentialsException("Not Refresh Token");
////
////        MemberEntity member = tokenEntity.getMember();
////        String newAccessToken  = jwtUtil.createAccessToken(member);
////        String newRefreshToken = jwtUtil.generateRefreshToken(member.getMid());
////
////        // DB 갱신 (Refresh Token Rotation)
////        tokenEntity.setRefreshToken(newRefreshToken);
////        refreshTokenRepository.save(tokenEntity);
////
////        return new TokenResponse(newAccessToken, member);
////    }
//    // RefreshTokenService.java
//
//
//    public TokenResponse reissueAccessToken(String refreshToken) {
////                                       ↑
////                              클라이언트가 보낸 토큰
//
//        // 1. 토큰에서 mid 추출
//        String mid = jwtUtil.getUserId(refreshToken);
//        //           ↑ "member1" 추출
//
//        // 2. Redis에서 저장된 토큰 조회  --------------------------------!!!!!!!!!!!!!!!
//        String stored = redisTemplate.opsForValue()
//                .get("refresh:" + mid);
//        //    ↑ Redis에 저장된 토큰
//
//        // 3. 클라이언트 토큰 vs Redis 토큰 비교
//        if (stored == null || !stored.equals(refreshToken))
//            throw new BadCredentialsException("Invalid Refresh Token");
//        //  stored    = Redis에 저장된 토큰
//        //  refreshToken = 클라이언트가 보낸 토큰
//        //  둘이 다르면 → 탈취된 토큰
//
//        // 4. JWT 서명·만료 검증
//        if (!jwtUtil.validateToken(refreshToken))
//            throw new BadCredentialsException("Refresh Token 만료");
//
//        // 5. 새 토큰 발급
//        MemberEntity member = memberRepository.findById(mid).orElseThrow();
//        String newAccessToken  = jwtUtil.createAccessToken(member);
//        String newRefreshToken = jwtUtil.generateRefreshToken(mid);
//
//        // 6. Redis 교체 (Rotation)
//        save(mid, newRefreshToken);
//
//        return new TokenResponse(newAccessToken, member);
//    }
//
//    // 로그아웃 — 모든 토큰 삭제
//    public void logout(String mid) {
//        redisTemplate.delete("refresh:" + mid);
//    }
//}
//
