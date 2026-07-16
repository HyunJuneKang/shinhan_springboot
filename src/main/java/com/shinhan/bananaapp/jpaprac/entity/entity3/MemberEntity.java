package com.shinhan.bananaapp.jpaprac.entity.entity3;

import com.shinhan.bananaapp.jpaprac.entity.entity2.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity extends BaseEntity {
    @Id
    private String mid;           // 회원 ID (PK)
    private String mname;
    private String mpassword;     //SpringSecurity는 암호화 되지 않으면 사용 불가
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole mrole;
}
