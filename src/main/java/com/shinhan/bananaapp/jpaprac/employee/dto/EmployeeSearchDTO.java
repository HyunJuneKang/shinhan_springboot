package com.shinhan.bananaapp.jpaprac.employee.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeSearchDTO {
    private String keyword;
    private Integer departmentId;
    private Long minSalary;
    private Long maxSalary;
}
