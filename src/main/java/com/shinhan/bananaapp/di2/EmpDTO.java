package com.shinhan.bananaapp.di2;

import lombok.*;

@AllArgsConstructor@NoArgsConstructor
@Builder@Setter@Getter
@ToString
public class EmpDTO {
    int empId;
    String empName;
    Long salary;
}
