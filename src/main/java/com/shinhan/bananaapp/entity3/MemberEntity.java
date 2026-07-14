package com.shinhan.bananaapp.entity3;

import com.shinhan.bananaapp.entity2.BaseEntity;
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
    private String mpassword;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole mrole;
}
