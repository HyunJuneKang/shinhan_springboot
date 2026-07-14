package com.shinhan.bananaapp.jpaprac.employee.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeDTO {

    private Integer employeeId;
    private String name;
    private String email;
    private Integer departmentId;
    private Long salary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}