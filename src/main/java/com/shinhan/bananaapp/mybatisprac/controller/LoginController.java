package com.shinhan.bananaapp.mybatisprac.controller;

import com.shinhan.bananaapp.mybatisprac.prev.MemberDTO;
import com.shinhan.bananaapp.mybatisprac.jdbc.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Enumeration;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    final MemberService memberService;

    @GetMapping("/login")
    public String getLogin(){
       return "/auth/login";
    }
    @PostMapping("/login")
    public String postLogin(@ModelAttribute MemberDTO member,
                            @RequestParam(name = "redirectURL", required = false) String redirectURL,
                            RedirectAttributes attr,
                            HttpSession session) {
        MemberDTO loginMember = memberService.login(member);

        if (loginMember == null) {
            attr.addFlashAttribute("error", "아이디나 비밀번호가 잘못됨 ㅅㄱ");
            if (redirectURL != null && !redirectURL.isBlank()) {
                attr.addAttribute("redirectURL", redirectURL);
            }
            return "redirect:/auth/login";
        }

        session.setAttribute("loginMember", loginMember);
        session.setMaxInactiveInterval(60 * 10);

        if (redirectURL != null && !redirectURL.isBlank()) {
            return "redirect:" + redirectURL;
        }
        return "redirect:/auth/mypage";
    }
    @GetMapping("/logout")
    public String logOut(HttpSession session){
        session.invalidate();
        return "redirect:/auth/login";
    }
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {

        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");

        if (member == null) {
            return "redirect:/auth/login?redirectURL=/auth/mypage";
        }

        model.addAttribute("member", member);
        return "/auth/mypage";
    }

    @GetMapping("/session-check")
    @ResponseBody
    public String sessionCheck(HttpSession session) {
        StringBuilder sb = new StringBuilder();

        sb.append("session id = ").append(session.getId()).append("<br>");
        sb.append("maxInactiveInterval = ")
                .append(session.getMaxInactiveInterval())
                .append("초<br><br>");

        Enumeration<String> names = session.getAttributeNames();

        if (!names.hasMoreElements()) {
            sb.append("세션에 저장된 값 없음");
            return sb.toString();
        }

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object value = session.getAttribute(name);

            sb.append(name)
                    .append(" = ")
                    .append(value)
                    .append("<br>");
        }

        return sb.toString();
    }
}
