package com.shinhan.bananaapp.security.jwt;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString @Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//table이 이름은 refresh_token_entity
//JWT 토큰은 AccessToken(요청시마다 들고다님,  보안주의 , 탈취되면 위험 , 지정 x , 유효시간 짧게)
// AccessToken 재발급받을때 필요한 토큰은 RefreshToken ...DB에 저장할 목적
public class RefreshTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 단방향 연관관계 (MemberEntity 참조)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(nullable = false, unique = true, length = 500)
    private String refreshToken;
}
