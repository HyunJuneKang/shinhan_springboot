package com.shinhan.bananaapp.mapper;

import com.shinhan.bananaapp.dto.JobDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobMapper {
    List<JobDTO> findAll();
}
