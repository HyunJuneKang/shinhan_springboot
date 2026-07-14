package com.shinhan.bananaapp.mybatisprac.controller;

import com.shinhan.bananaapp.mybatisprac.prev.DeptDTO;
import com.shinhan.bananaapp.mybatisprac.prev.EmpDTO;
import com.shinhan.bananaapp.mybatisprac.prev.EmpSearchDTO;
import com.shinhan.bananaapp.mybatisprac.prev.JobDTO;
import com.shinhan.bananaapp.mybatisprac.service.EmpServiceMyBatis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/emp")
@Controller("empController2")
public class EmpController {

    final private EmpServiceMyBatis empService;
    @GetMapping("/emplist")
    public String emplist() {
        return "emp/emplist";
    }
    @ResponseBody
    @GetMapping("/list")
    public List<EmpDTO> empSelectAll() {
        return empService.selectAllService();
    }
    @ResponseBody
    @GetMapping("/job/list")
    public List<JobDTO> jobSelectAll() {
        return empService.jobSelectAllService();
    }
    @ResponseBody
    @GetMapping("/dept/list")
    public List<DeptDTO> deptList() {
        return empService.deptSelectAllService();
    }
    @ResponseBody
    @GetMapping("/detail")
    public EmpDTO detail(@RequestParam("empid") Long empid) {
        return empService.selectByIdService(empid);
    }
    @ResponseBody
    @PostMapping("/insert")
    public Map<String, Object> insert(EmpDTO emp) {
        int result = empService.insertService(emp);
        return Map.of("result", result);
    }

    @ResponseBody
    @PostMapping("/update")
    public Map<String, Object> update(EmpDTO emp) {
        int result = empService.updateService(emp);
        return Map.of("result", result);
    }

    @ResponseBody
    @PostMapping("/delete")
    public int delete(@RequestParam("empid") Long empid) {
        return empService.delete(empid);
    }

    @ResponseBody
    @PostMapping("/condition")
    public List<EmpDTO> condition(EmpSearchDTO searchDTO) {
        return empService.findByCondition(searchDTO);
    }
    @ResponseBody
    @PostMapping("/search")
    public List<EmpDTO> search(@RequestParam("fname") String name) {
        return empService.selectByNameService(name);
    }
}
