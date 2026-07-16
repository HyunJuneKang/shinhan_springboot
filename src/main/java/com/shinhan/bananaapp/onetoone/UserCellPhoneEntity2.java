package com.shinhan.bananaapp.onetoone;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "t_usercellphone2")
public class UserCellPhoneEntity2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id")
    Long phoneid;
    String phoneNumber;
    String model;

    //비식별자로 대상 테이블에서 참조하기
    //칼람은 대상 테이블에 생성(RDB와 같음)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    UserEntity2 user;
}
