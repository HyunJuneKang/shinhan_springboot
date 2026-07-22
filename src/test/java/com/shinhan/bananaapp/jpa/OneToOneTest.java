package com.shinhan.bananaapp.jpa;

import com.shinhan.bananaapp.onetoone.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OneToOneTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserCellPhoneRepository2 phoneRepo2;
    @Autowired
    UserRepository3 userRepository3;



    @Test
    void f_insert3(){
        UserEntity3 user = UserEntity3.builder()
                .userId("young")
                .userName("도영")
                .build();
        UserCellPhoneEntity3 phone = UserCellPhoneEntity3.builder()
                .phoneNumber("02-1234-6789")
                .model("ABC")
                .user(user)
                .build();
        user.setPhone(phone);
        userRepository3.save(user);
    }

    //대상 table을 통해 접근
    @Test
    void f_insert2(){
        UserEntity2 user = UserEntity2.builder()
                .userid("young")
                .username("도영")
                .build();
        UserCellPhoneEntity2 phone = UserCellPhoneEntity2.builder()
                .phoneNumber("02-1234-6789")
                .model("ABC")
                .user(user)
                .build();
        phoneRepo2.save(phone);
    }
    //주 table을 통해 부를 접근
    @Test
    void f_insert(){
        UserCellPhoneEntity phone = UserCellPhoneEntity.builder()
                .model("4324")
                .phoneNumber("1232")
                .build();
        UserEntity user = UserEntity
                .builder()
                .userid("zz2")
                .username("찔레2")
                .phone(phone)
                .build();
        userRepository.save(user);
    }
}
