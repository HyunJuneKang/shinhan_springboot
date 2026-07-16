package com.shinhan.bananaapp.onetoone;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user1")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    String userid;
    @Column(name = "user_name")
    String username;
    //1. 주테이블에서 참조하기
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phone_id")
    UserCellPhoneEntity phone;

}
