package com.shinhan.bananaapp.mapper;

import com.shinhan.bananaapp.dto.DeptDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptMapper {
    List<DeptDTO> findAll();
}
