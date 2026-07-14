package com.shinhan.bananaapp.dto.prev;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeptDTO {
    Integer departmentId;
    String departmentName;
    Integer managerId;
    Integer  locationId;
}
