package com.shinhan.bananaapp.security;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import com.shinhan.bananaapp.jpaprac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // Spring Security가 로그인 시 자동 호출
    // SecurityUser class (개발자가 구현 ) <----- User class <---- UserDetails
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new SecurityUser(member);
    }

    // 원본 MemberEntity 조회 (JWT AuthFilter에서 사용)
    public MemberEntity loadDomainMember(String mid) {
        return memberRepository.findById(mid).orElseThrow();
    }

    // 회원 가입 — 비밀번호 BCrypt 암호화
    public MemberEntity joinUser(MemberEntity member) {
        member.setMpassword(passwordEncoder.encode(member.getMpassword()));
        return memberRepository.save(member);
    }
}
