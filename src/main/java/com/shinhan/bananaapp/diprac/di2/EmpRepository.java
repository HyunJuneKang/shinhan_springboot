package com.shinhan.bananaapp.diprac.di2;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
//타입이 같으면 다른 Component 에서 자동 Injection(@Autowired)
//같은 타입이 여러개 존재하면 이름 구별(@Qualifier)
@Repository
public class EmpRepository {
    List<EmpDTO> empList = new ArrayList<>();
    EmpRepository(){
        EmpDTO emp1 = new EmpDTO();
        emp1.setEmpId(1);
        emp1.setEmpName("홍길동");
        emp1.setSalary(1000L);

        EmpDTO emp2 = new EmpDTO(2,"병국",2000L);
        EmpDTO emp3 = EmpDTO
                .builder()
                .empId(3)
                .empName("민준")
                .salary(3000L)
                .build();
        empList.add(emp1);
        empList.add(emp2);
        empList.add(emp3);
    }

    public List<EmpDTO> selectAll(){
        return empList;
    };

    public int insertEmp(EmpDTO emp) {
        if (emp == null)
            return -1;
        for(EmpDTO curEmp : empList){
            if(curEmp.getEmpId() == emp.getEmpId())
                return -1;
        }
        empList.add(emp);
        return 1;
    }

    public EmpDTO selectById(int id) {
        EmpDTO emp = null;
        for(EmpDTO curEmp : empList){
            if(curEmp.getEmpId() == id)
                emp = curEmp;
        }
        return emp;
    }

    public int updateEmp(EmpDTO emp) {
        EmpDTO selEmp = selectById(emp.getEmpId());
        if (selEmp == null)
            return -1;
        selEmp.setSalary(emp.getSalary());
        selEmp.setEmpName(emp.getEmpName());
        return 1;
    }
}
