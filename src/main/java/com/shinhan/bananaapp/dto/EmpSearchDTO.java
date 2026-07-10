package com.shinhan.bananaapp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter@Setter@ToString
public class EmpSearchDTO {

    private Integer employeeId;     // 사번 검색
    private String empName;         // 이름 검색어
    private Integer departmentId;   // 부서 ID
    private String jobId;           // 직책 ID
    private Date hireDate;          // 입사일
    private Double minSalary;       // 최소 급여
}
