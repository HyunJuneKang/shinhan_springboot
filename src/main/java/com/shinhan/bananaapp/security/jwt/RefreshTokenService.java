package com.shinhan.bananaapp.security.jwt;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import com.shinhan.bananaapp.jpaprac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public RefreshTokenEntity findByMid(MemberEntity member) {
        return refreshTokenRepository.findByMember(member);
    }

    //!!!수정
    // 로그인 성공 시 RefreshToken DB 저장
    public void save(String mid, String refreshToken) {
        MemberEntity member = memberRepository.findById(mid)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        // 기존 토큰 조회
        RefreshTokenEntity entity =
                refreshTokenRepository.findByMember(member);

        if (entity != null) {
            // 기존 토큰 UPDATE
            entity.setRefreshToken(refreshToken);
            refreshTokenRepository.save(entity);
        } else {
            // 신규 INSERT
            refreshTokenRepository.save(
                    RefreshTokenEntity.builder()
                            .member(member)
                            .refreshToken(refreshToken)
                            .build()
            );
        }
    }

    // RefreshToken 검증 + AccessToken 재발급
    @Transactional
    public TokenResponse reissueAccessToken (String refreshToken){
        RefreshTokenEntity tokenEntity =
                refreshTokenRepository.findByRefreshToken(refreshToken)
                        .orElseThrow(() -> new BadCredentialsException("Invalid Refresh Token"));

        if (!jwtUtil.validateToken(refreshToken))
            throw new BadCredentialsException("Refresh Token Invalid");
        if (!jwtUtil.isRefreshToken(refreshToken))
            throw new BadCredentialsException("Not Refresh Token");

        MemberEntity member = tokenEntity.getMember();
        String newAccessToken = jwtUtil.createAccessToken(member);
        String newRefreshToken = jwtUtil.generateRefreshToken(member.getMid());

        // DB 갱신 (Refresh Token Rotation)
        tokenEntity.setRefreshToken(newRefreshToken);
        refreshTokenRepository.save(tokenEntity);

        return new TokenResponse(newAccessToken, member);
    }

    // 로그아웃 — 모든 토큰 삭제
    public void logout (String mid){
        refreshTokenRepository.deleteByMemberMid(mid);
    }
}