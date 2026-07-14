package com.shinhan.bananaapp.controller.mybatis;

import com.shinhan.bananaapp.common.ApiResponse;
import com.shinhan.bananaapp.dto.prev.AccountDTO;
import com.shinhan.bananaapp.service.mybatis.AccountServiceUsingMyBatis;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RequestMapping,
//@Controller // 요청받아서 처리하고 응답은 /templates/?????.html
@RestController//요청받고 처리하고 응답은 Data를 JSON 변경엣 (Jackson Library) ResponseBody 로 보낸다
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountRestController {

    final private AccountServiceUsingMyBatis accountService;
    @PutMapping
    public String update(@RequestBody AccountDTO acc){
        int result = accountService.updateService(acc);
        return result == 1 ? "수정성공": result == -1 ? "db존재 x" : "입력값 오류";
    }
    @PostMapping
    public ResponseEntity<ApiResponse<AccountDTO>> insert(@RequestBody AccountDTO acc){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(acc));
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long accId){
        int result = accountService.delete(accId);
        return result == 1 ? "삭제성공": result == -1 ? "삭제대상없음" : "입력값 오류";
    }
    //JSP/Servlet => SpringFramework => SpringBoot
    //Restful방식은 주소가 /로 끝나면 안된다
//    @GetMapping //각각의 개별요청은 method에 쓴다
//    public List<AccountDTO> selectAll(){
//        return accountService.selectAllService();
//    }
    @GetMapping("/{id}")
    public ApiResponse selectById(@PathVariable("id") Long accId){
        AccountDTO acc = accountService.selectByIdService(accId);
        return new ApiResponse(true,"OK",acc);
    }

}
