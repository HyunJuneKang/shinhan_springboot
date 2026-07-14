package com.shinhan.bananaapp.jpaprac.employee.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 100)
    private String email;
    private Integer departmentId;
    private Long salary;
}