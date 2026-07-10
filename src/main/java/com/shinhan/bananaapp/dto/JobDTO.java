package com.shinhan.bananaapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder@Setter
@Getter
public class JobDTO {
    String jobId;
    String jobTitle;
    Integer minSalary;
    Integer maxSalary;
}
