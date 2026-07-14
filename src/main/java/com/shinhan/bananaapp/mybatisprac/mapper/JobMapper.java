package com.shinhan.bananaapp.mybatisprac.mapper;

import com.shinhan.bananaapp.mybatisprac.prev.JobDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobMapper {
    List<JobDTO> findAll();
}
