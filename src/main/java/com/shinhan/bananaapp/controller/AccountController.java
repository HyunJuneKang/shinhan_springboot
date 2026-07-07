package com.shinhan.bananaapp.controller;

import com.shinhan.bananaapp.di2.EmpDTO;
import com.shinhan.bananaapp.dto.AccountDTO;
import com.shinhan.bananaapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//사용자 요청 --> Controller-->Service-->Repository-->DB
//사용자 응답 <-- (template/....html 파일을 만들어서 보냄)
//Thymeleaf 은 서버사이드 엔진으로 html으로 그대로 가공하기 때문에 가독성이 유리
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    final AccountService accountService;
    //이스터에그
    @GetMapping("/hungry")
    public String retrieve(Model model){
        //model : controller와 html간의 공유 공간
        model.addAttribute("myname","jin");
        model.addAttribute("emp", EmpDTO.builder().empId(4).empName("강").salary(1000L).build());
        model.addAttribute("comment1","<h1>성실!!!!</h1>");
        model.addAttribute("comment2","<script>alert('배고파');</script>");
        return "account/list";
    }
    @GetMapping
    public String f_selectAll(Model model){
        model.addAttribute("acclist",accountService.selectAllService());
        return "account/list";
    }

    @GetMapping("/insert")
    public String f_insertForm(Model model){
        model.addAttribute("accountDTO",new AccountDTO());
        return "account/insert";
    }

    @GetMapping("/detail")
    public String f_detail(@RequestParam("id") Long id,Model model){
        AccountDTO account = accountService.selectById(id);

        model.addAttribute("acc", account);
        model.addAttribute("type", "RequestParam");
        model.addAttribute("accountDTO", account);
        return "account/detail";
    }
    @GetMapping("/detail/{id}")
    public String f_detail2(@PathVariable("id") Long id,Model model){
        AccountDTO account = accountService.selectById(id);

        model.addAttribute("acc", account);
        model.addAttribute("type", "RequestParam");
        model.addAttribute("accountDTO", account);
        return "account/detail";
    }
    @PostMapping("/update")
    public String f_update(@ModelAttribute AccountDTO account, RedirectAttributes rttr){
        int result = accountService.updateService(account);
        System.out.println(result);
        System.out.println("들어옴" + account.getBalance());
        if (result == 1) {
            rttr.addFlashAttribute("msg", "수정에 성공되었습니다.");
        } else {
            rttr.addFlashAttribute("msg", "수정에 실패하셨습니다.");
        }

        return "redirect:/account";
    }

    @PostMapping("/insert")
    public String f_insert(@ModelAttribute AccountDTO account, RedirectAttributes rttr){
        int result = accountService.insertService(account);
        System.out.println(result);
        System.out.println("들어옴" + account.getBalance());
        if (result == 1) {
            rttr.addFlashAttribute("msg", "입력에 성공되었습니다.");
        } else {
            rttr.addFlashAttribute("msg", "입력에 실패하셨습니다.");
        }

        return "redirect:/account";
    }
}
