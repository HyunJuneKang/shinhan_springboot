package com.shinhan.bananaapp.di2;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmpController {
    EmpService empService;
    EmpController(EmpService empService){
        this.empService = empService;
    }

    @GetMapping
    public List<EmpDTO> selectAll(){
        return empService.selectAllService();
    }
    @PostMapping
    public String insertEmp(@RequestBody EmpDTO emp){
        return empService.insertEmpService(emp) == 1 ? "성공" : "실패";
    }
}
