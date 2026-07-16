package com.shinhan.bananaapp.onetoone;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user3")
@ToString(exclude = "phone")
public class UserEntity3 {

    @Id
    @Column(name = "user_id")
    String userId;

    @Column(name = "user_name")
    String userName;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    UserCellPhoneEntity3 phone;

}