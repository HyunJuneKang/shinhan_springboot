package com.shinhan.bananaapp.mybatisprac.mapper;

import com.shinhan.bananaapp.mybatisprac.prev.DeptDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptMapper {
    List<DeptDTO> findAll();
}
