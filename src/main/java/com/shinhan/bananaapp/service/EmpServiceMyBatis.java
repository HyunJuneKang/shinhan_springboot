package com.shinhan.bananaapp.service;

import com.shinhan.bananaapp.dto.DeptDTO;
import com.shinhan.bananaapp.dto.EmpDTO;
import com.shinhan.bananaapp.dto.EmpSearchDTO;
import com.shinhan.bananaapp.dto.JobDTO;
import com.shinhan.bananaapp.mapper.AccountMapper;
import com.shinhan.bananaapp.mapper.DeptMapper;
import com.shinhan.bananaapp.mapper.EmpMapper;
import com.shinhan.bananaapp.mapper.JobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpServiceMyBatis {
    final private EmpMapper empMapper;
    final private JobMapper jobMapper;
    final private DeptMapper deptMapper;
    private static final long MAX_TRANSFER_AMOUNT = 10_000_000L;
    final AccountMapper accountMapper;
    /**
     * 전체 직원 직급 부서 조회
     */
    public List<EmpDTO> selectAllService() {
        return empMapper.findAll();
    }

    public List<JobDTO> jobSelectAllService() {
        return jobMapper.findAll();
    }

    public List<DeptDTO> deptSelectAllService() {
        return deptMapper.findAll();
    }

    /**
     * 직원번호로 조회
     */
    public EmpDTO selectByIdService(Long id) {
        return empMapper.findById(id);
    }

    /**
     * 조건 검색
     */
    public List<EmpDTO> findByCondition(EmpSearchDTO search) {
        return empMapper.findByCondition(search);
    }

    /**
     * 직원 등록
     */
    public int insertService(EmpDTO emp) {
        int result = empMapper.insert(emp);
        if (result != 1) {
            throw new RuntimeException("직원 등록에 실패했습니다.");
        }
        return result;
    }

    /**
     * 직원 수정
     */
    public int updateService(EmpDTO emp) {
        // 존재 여부 확인...없으면 예외를 발생
        getEmp(Long.valueOf(emp.getEmployeeId()));
        int result = empMapper.update(emp);
        if (result != 1) {
            throw new RuntimeException("직원 수정에 실패했습니다.");
        }
        return result;
    }

    /**
     * 직원 삭제
     */
    public int delete(Long id) {
        // 존재 여부 확인
        getEmp(id);
        int result = empMapper.delete(id);
        if (result != 1) {
            throw new RuntimeException("계좌 삭제에 실패했습니다.");
        }
        return result;
    }
    private void getEmp(Long id) {
        EmpDTO emp = empMapper.findById(id);
        if (emp == null) {
            throw new RuntimeException("해당 직원이 존재하지 않습니다.");
        }
    }

    public List<EmpDTO> selectByNameService(String name) {
        return empMapper.selectByName(name);
    }
}
