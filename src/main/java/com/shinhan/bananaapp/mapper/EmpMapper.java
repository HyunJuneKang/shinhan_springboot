package com.shinhan.bananaapp.mapper;

import com.shinhan.bananaapp.dto.EmpDTO;
import com.shinhan.bananaapp.dto.EmpSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpMapper {

    List<EmpDTO> findAll();

    EmpDTO findById(Long id);

    List<EmpDTO> findByCondition(EmpSearchDTO search);

    int insert(EmpDTO emp);

    int update(EmpDTO emp);

    int delete(Long id);

    List<EmpDTO> selectByName(String empName);
}
