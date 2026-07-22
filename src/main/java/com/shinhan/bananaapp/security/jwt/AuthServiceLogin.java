package com.shinhan.bananaapp.security.jwt;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import com.shinhan.bananaapp.jpaprac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service @RequiredArgsConstructor
public class AuthServiceLogin {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse login(MemberEntity dto) {
        MemberEntity member = memberRepository.findById(dto.getMid())
                .orElseThrow(() -> new UsernameNotFoundException("Mid이 존재하지 않습니다."));

        if (!encoder.matches(dto.getMpassword(), member.getMpassword()))
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");

        // Access Token 발급
        String accessToken = jwtUtil.createAccessToken(member);

        // Refresh Token: 기존 토큰 재사용 or 신규 발급
        RefreshTokenEntity refreshTokenEntity =
                refreshTokenService.findByMid(member);
        if (refreshTokenEntity == null) {
            String newRefreshToken = jwtUtil.generateRefreshToken(member.getMid());
            refreshTokenService.save(member.getMid(), newRefreshToken);
        }

        //member.setMpassword(null);  // 비밀번호 응답에서 제거
        return new TokenResponse(accessToken, member);
    }


    public void logout(String mid) {
        refreshTokenService.logout(mid);
        log.info("[logout] RefreshToken 삭제 완료: {}", mid);
    }

}
