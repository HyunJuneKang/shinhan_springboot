package com.shinhan.bananaapp.controller;

import com.shinhan.bananaapp.dto.AccountDTO;
import com.shinhan.bananaapp.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RequestMapping,
//@Controller // 요청받아서 처리하고 응답은 /templates/?????.html
@RestController//요청받고 처리하고 응답은 Data를 JSON 변경엣 (Jackson Library) ResponseBody 로 보낸다
@RequestMapping("/account")
public class AccountController {

    final private AccountService accountService;
    AccountController( AccountService accountService){
        this.accountService = accountService;
    }
    @PutMapping
    public String update(@RequestBody AccountDTO acc){
        int result = accountService.updateService(acc);
        return result == 1 ? "수정성공": result == -1 ? "db존재 x" : "입력값 오류";
    }
    @PostMapping
    public String insert(@RequestBody AccountDTO acc){
        int result = accountService.insertService(acc);
        return result == 1 ? "입력성공": result == 2 ? "수정성공" : "입력값 오류";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long accId){
        int result = accountService.deleteService(accId);
        return result == 1 ? "삭제성공": result == -1 ? "삭제대상없음" : "입력값 오류";
    }
    //JSP/Servlet => SpringFramework => SpringBoot
    //Restful방식은 주소가 /로 끝나면 안된다
    @GetMapping //각각의 개별요청은 method에 쓴다
    public List<AccountDTO> selectAll(){
        return accountService.selectAllService();
    }
    @GetMapping("/{id}")
    public AccountDTO selectById(@PathVariable("id") Long accId){
        return accountService.selectById(accId);
    }
}
