package com.shinhan.bananaapp.security.jwt;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;
    private MemberEntity member;
}
