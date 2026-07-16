package com.shinhan.bananaapp;


import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberRole;
import com.shinhan.bananaapp.jpaprac.entity.entity3.ProfileEntity;
import com.shinhan.bananaapp.jpaprac.repository.MemberRepository;
import com.shinhan.bananaapp.jpaprac.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
public class ManyToOneTest {
    @Autowired
    MemberRepository memberRepo;
    @Autowired
    ProfileRepository profileRepo;
    @Transactional
    @Test
    void f_selectAllProfile2(){
        MemberEntity member = MemberEntity
                .builder()
                .mid("injin")
                .build();
        profileRepo.findByMember(member).forEach(p->{
            System.out.println(p.getMember());
        });
    }
    @Transactional
    @Test
    void f_selectAllProfile(){
        profileRepo.findAll().forEach(p->{
            System.out.println(p);
            System.out.println(p.getMember());
            System.out.println("--------------");
        });
    }

    @Test
    void insertProfile(){
        //mid = 'suk' 인 멤버의 profile 을 5개 insert 해보자
        String mid = "injin";
//        MemberEntity member = memberRepo.findById(mid).orElse(null);
        MemberEntity member = memberRepo.getReferenceById(mid);
        IntStream.rangeClosed(1,3).forEach(i->{
            ProfileEntity profile = ProfileEntity.builder()
                    .pfile("flower" + i + ".png")
                    .member(member)
                    .build();
            profileRepo.save(profile);
        });
    }

    @Test
    void f_selectAllMember(){
        memberRepo.findAll().forEach(System.out::println);
    }

    @Test
    void f_insertMember(){
        //3명 Insert
        MemberEntity m1 = MemberEntity.builder()
                .mid("injin")
                .mname("지인진")
                .mpassword("1234")
                .mrole(MemberRole.ADMIN)
                .build();
        MemberEntity m2 = MemberEntity.builder()
                .mid("bean")
                .mname("박채빈")
                .mpassword("1234")
                .mrole(MemberRole.MANAGER)
                .build();
        MemberEntity m3 = MemberEntity.builder()
                .mid("suk")
                .mname("김민석")
                .mpassword("1234")
                .mrole(MemberRole.USER)
                .build();
        memberRepo.save(m1);
        memberRepo.save(m2);
        memberRepo.save(m3);
    }
}
