package com.shinhan.bananaapp.security;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    MemberService memberService;

    @GetMapping("/login")
    public void login() {}

    @GetMapping("/loginSuccess")
    public void loginSuccess() {}

    @GetMapping("/accessDenied")
    public void accessDenied() {}

    // 회원가입 페이지
    @GetMapping("/signup")
    public String joinForm() {
        return "auth/joinForm";
    }

    // 회원가입 처리
    @ResponseBody
    @PostMapping("/joinProc")
    public String joinProc(MemberEntity member) { //신규 입력
        MemberEntity newMember = memberService.joinUser(member);
        return newMember != null && member.getMid().equals(newMember.getMid())
                ? "register OK" : "가입실패";
    }
}
