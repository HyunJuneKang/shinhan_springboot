package com.shinhan.bananaapp.diprac.di2;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpService {
    EmpRepository empRepo;
    EmpService(EmpRepository empRepo){
        this.empRepo = empRepo;
    }
    public List<EmpDTO> selectAllService(){
        return empRepo.selectAll();
    }

    public int insertEmpService(EmpDTO emp) {
        return empRepo.insertEmp(emp);
    }

    public EmpDTO selectByIdService(int id) {
        return empRepo.selectById(id);
    }

    public int updateService(EmpDTO emp) {
        return empRepo.updateEmp(emp);
    }
}
