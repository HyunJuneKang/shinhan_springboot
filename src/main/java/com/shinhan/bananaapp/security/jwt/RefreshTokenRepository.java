package com.shinhan.bananaapp.security.jwt;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshTokenEntity, Long> {

    // RefreshToken 문자열로 조회 (재발급 시)
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    // 특정 회원의 Refresh Token 조회
    RefreshTokenEntity findByMember(MemberEntity member);

    // 회원 전체 로그아웃 / 탈퇴 시 삭제
    void deleteByMemberMid(String mid);

    Long countByMember(MemberEntity mid);
}
