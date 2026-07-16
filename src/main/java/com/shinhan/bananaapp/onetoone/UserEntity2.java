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
@Table(name = "user2")
public class UserEntity2 {
    @Id
    @Column(name = "user_id")
    String userid;
    @Column(name = "user_name")
    String username;

}
