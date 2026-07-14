package com.shinhan.bananaapp.jpaprac.employee.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Department {
    @Id
    private Long departmentId;
}