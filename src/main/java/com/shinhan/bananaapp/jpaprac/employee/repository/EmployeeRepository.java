package com.shinhan.bananaapp.jpaprac.employee.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.shinhan.bananaapp.jpaprac.employee.dto.EmployeeSearchDTO;
import com.shinhan.bananaapp.jpaprac.employee.entity.EmployeeEntity;
import com.shinhan.bananaapp.jpaprac.employee.entity.QEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EmployeeRepository
        extends JpaRepository<EmployeeEntity, Integer>,
        QuerydslPredicateExecutor<EmployeeEntity> {

    default Predicate makePredicate(
            EmployeeSearchDTO condition
    ) {

        QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (condition == null) {
            return builder;
        }

        if (condition.getKeyword() != null
                && !condition.getKeyword().isBlank()) {
            String keyword = condition.getKeyword();
            builder.and(
                    employee.name.containsIgnoreCase(keyword)
                            .or(
                                    employee.email
                                            .containsIgnoreCase(keyword)
                            )
            );
        }

        if (condition.getDepartmentId() != null) {
            builder.and(
                    employee.departmentId.eq(
                            condition.getDepartmentId()
                    )
            );
        }

        if (condition.getMinSalary() != null) {

            builder.and(
                    employee.salary.goe(
                            condition.getMinSalary()
                    )
            );
        }

        if (condition.getMaxSalary() != null) {

            builder.and(
                    employee.salary.loe(
                            condition.getMaxSalary()
                    )
            );
        }

        return builder;
    }
}