package com.shinhan.bananaapp.diprac.di2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
//@RestController
@RequestMapping("/employees")
public class EmpController {
    EmpService empService;
    EmpController(EmpService empService){
        this.empService = empService;
    }

//    @GetMapping
//    public List<EmpDTO> selectAll(){
//        return empService.selectAllService();
//    }
//    @PostMapping
//    public String insertEmp(@RequestBody EmpDTO emp){
//        return empService.insertEmpService(emp) == 1 ? "성공" : "실패";
//    }
    @GetMapping
    public String selectAll(Model model){
        List<EmpDTO> empList = empService.selectAllService();
        model.addAttribute("empList",empList);
        return "emp/list";
    }
    @GetMapping("detail")
    public String insertEmp(@RequestParam("id") int id, Model model){
        EmpDTO emp = empService.selectByIdService(id);
        model.addAttribute("emp",emp);
        model.addAttribute("type","RequestParam");
        return "emp/detail";
    }
    @GetMapping("/detail/{id}")
    public String f_detail2(@PathVariable("id") int id, Model model){
        EmpDTO emp = empService.selectByIdService(id);
        model.addAttribute("emp",emp);
        model.addAttribute("type","RequestParam");
        return "emp/detail";
    }
    @PostMapping("/update")
    public String f_update(@ModelAttribute EmpDTO emp, RedirectAttributes rttr){
        int result = empService.updateService(emp);
        if (result == 1) {
            rttr.addFlashAttribute("msg", "수정에 성공되었습니다.");
        } else {
            rttr.addFlashAttribute("msg", "수정에 실패하셨습니다.");
        }
        return "redirect:/employees";
    }
    @PostMapping("/insert")
    public String f_insert(@ModelAttribute EmpDTO emp, RedirectAttributes rttr){
        int result = empService.insertEmpService(emp);
        if (result == 1) {
            rttr.addFlashAttribute("msg", "입력에 성공되었습니다.");
        } else {
            rttr.addFlashAttribute("msg", "입력에 실패하셨습니다.");
        }
        return "redirect:/employees";
    }
    @GetMapping("/insert")
    public String f_insert_form(Model model){
        model.addAttribute("emp",new EmpDTO());
        return "emp/insert";
    }
}
