package com.shinhan.bananaapp.service;

import com.shinhan.bananaapp.dto.MemberDTO;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public MemberDTO login(MemberDTO member){
        //DB가서 해당 Member 정보를 가져온다.
        return MemberDTO.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .mRole("MANAGER")
                .mName("홍길동")
                .build();
    }
}
