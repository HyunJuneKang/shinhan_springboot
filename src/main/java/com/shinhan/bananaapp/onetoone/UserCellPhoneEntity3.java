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
@Table(name = "t_usercellphone3")
public class UserCellPhoneEntity3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String dummyId;
    //(대상테이블에서 식별자로 사용)
    @MapsId //PK이면서 FK
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity3 user;

    String phoneNumber;
    String model;

}
