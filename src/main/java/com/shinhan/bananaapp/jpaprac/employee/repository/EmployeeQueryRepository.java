package com.shinhan.bananaapp.jpaprac.employee.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shinhan.bananaapp.jpaprac.employee.dto.EmployeeSearchDTO;
import com.shinhan.bananaapp.jpaprac.employee.entity.EmployeeEntity;
import com.shinhan.bananaapp.jpaprac.employee.entity.QEmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QEmployeeEntity employee =
            QEmployeeEntity.employeeEntity;

    public List<EmployeeEntity> search( EmployeeSearchDTO condition) {
        return queryFactory
                .selectFrom(employee)
                .where(
                        keywordContains(condition.getKeyword()),
                        departmentEq(condition.getDepartmentId()),
                        salaryGoe(condition.getMinSalary())
                )
                .fetch();
    }

    private BooleanExpression keywordContains( String keyword ) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return employee.name.containsIgnoreCase(keyword)
                .or(employee.email.containsIgnoreCase(keyword));
    }
    private BooleanExpression departmentEq( Integer departmentId ) {
        if (departmentId == null) {
            return null;
        }

        return employee.departmentId.eq(departmentId);
    }
    private BooleanExpression salaryGoe(Long minSalary ) {
        if (minSalary == null) {
            return null;
        }

        return employee.salary.goe(minSalary);
    }
}