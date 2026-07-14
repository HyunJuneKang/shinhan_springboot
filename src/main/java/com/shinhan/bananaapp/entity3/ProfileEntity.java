package com.shinhan.bananaapp.entity3;

import com.shinhan.bananaapp.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

// 한명의 Member는 여러개의 Profile을 갖는다.
// DB는 반드시 Profile이 참조한다. (FK)
// 자바는 Member가 참조가능 @OneTomany
// 자바는 Profile이 참조가능 @ManyToOne
// 자바는 양쪽 참조 가능 @OneToMany + @ManyToOne
// ProfileEntity — N쪽 (연관관계 주인)
@Entity
@Table(name = "profile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "member")   // LAZY 프록시 접근 방지
public class ProfileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;
    private String pfile;
    private boolean pcurrent;

    // FK 실제 관리 —@JoinColumn생략하면  member_mid 컬럼 자동 생성
    @ManyToOne (fetch = FetchType.LAZY)  //default값은 EAGER,  LAZY 필수!
    private MemberEntity member;
}

/*
fetch : 조회
--default : EAGER(즉시로딩)
    Profile이 3명 멤버의 data라면
    Profile 1번, Member 3번 select 된다.
--LAZY : (지연로딩)
    멤버는 select만 함
    LazyInitializationException 발생 , 발생 막으려면 @Transcational 추가
    문제점은 select 여러번 N+1 문제


 */